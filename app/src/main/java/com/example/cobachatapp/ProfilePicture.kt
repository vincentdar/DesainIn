package com.example.cobachatapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.io.ByteArrayOutputStream

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfilePicture.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfilePicture : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var user: User? = null
    private var imageURI: Uri = Uri.EMPTY
    private lateinit var  _ivFile: ImageView
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            _ivFile.setImageURI(data?.data)
            imageURI = data?.data as Uri
            Log.d("Gallery", data?.data.toString())

        }
    }

    fun chooseImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startForResult.launch(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_picture, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        if(arguments != null) {
            user = arguments?.getParcelable("current_user")
        }

        _ivFile = view.findViewById(R.id.ivProfilePhoto)
        val _btnPick = view.findViewById<Button>(R.id.btnPick)
        val _btnUpdate = view.findViewById<Button>(R.id.btnUpdate)

        _btnPick.setOnClickListener {
            chooseImage()
        }

        _btnUpdate.setOnClickListener {
            val userId = user?.userId.toString()
            val firestore = FirebaseFirestore.getInstance()
            firestore.collection("tbUsers").document(userId).update("profileImage", userId)
                .addOnSuccessListener {
                    // Upload to Storage
                    val bitmap = (_ivFile.drawable as BitmapDrawable).bitmap
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val data  = baos.toByteArray()

                    val storageRef = FirebaseStorage.getInstance().reference
                    val profileRef = storageRef.child("profileImage/" + userId)
                    val uploadTask = profileRef.putBytes(data)
                    uploadTask
                        .addOnSuccessListener {

                            profileRef.downloadUrl.addOnSuccessListener {
                                _ivFile.setImageResource(0)
                                val auth = FirebaseAuth.getInstance()
                                val displayProfileUpdates = UserProfileChangeRequest.Builder().setPhotoUri(it).build()

                                auth.currentUser!!.updateProfile(displayProfileUpdates)
                                //Toast.makeText(context, "Upload Profile Image Berhasil", Toast.LENGTH_SHORT).show()
                                MotionToast.createColorToast(requireActivity(), "Success",
                                    "Upload Profile Image Berhasil",
                                    MotionToastStyle.SUCCESS,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.SHORT_DURATION,
                                    ResourcesCompat.getFont(requireActivity(), R.font.gilroy_light))
                            }


                        }
                        .addOnFailureListener {
                            Log.d("Storage", it.toString())
                        }
                }
                .addOnFailureListener {
                    Log.d("Firestore", "Failed to update profileImage")
                    MotionToast.createColorToast(requireActivity(), "Error",
                        "Upload Profile Image Gagal",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.SHORT_DURATION,
                        ResourcesCompat.getFont(requireActivity(), R.font.gilroy_light))
                }


        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfilePicture.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfilePicture().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}