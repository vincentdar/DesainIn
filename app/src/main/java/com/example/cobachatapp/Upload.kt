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
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toFile
import androidx.core.view.isInvisible
import com.example.cobachatapp.Helper.DateHelper
import com.example.cobachatapp.Helper.GenerateUUID
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Upload.newInstance] factory method to
 * create an instance of this fragment.
 */
class Upload : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var userId: String? = null
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
    private var imageURI: Uri = Uri.EMPTY
    private lateinit var _ivFile: ImageView
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            _ivFile.setImageURI(data?.data)
            imageURI = data?.data as Uri
            Log.d("Gallery", data?.data.toString())

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            userId = param1
        }
        Log.d("Upload File", userId.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upload, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val _etCaption = view.findViewById<EditText>(R.id.etCaption)
        val _btnUpload = view.findViewById<Button>(R.id.btnUpload)
        val _tvFrUpload = view.findViewById<TextView>(R.id.tvFrUpload)
        _tvFrUpload.isInvisible = true
        val _btnChooseFile = view.findViewById<Button>(R.id.btnChooseFile)
        _ivFile = view.findViewById<ImageView>(R.id.ivFile)
        val _progressBar = view.findViewById<ProgressBar>(R.id.progressBar)

        _btnChooseFile.setOnClickListener {
            chooseImage()
        }

        _btnUpload.setOnClickListener {
            var str = _etCaption.text.toString()


            val tanggal = DateHelper.getCurrentDate()

            val post = dcPost(userId, str, tanggal)
            val uuid = GenerateUUID.generate()
            _progressBar.incrementProgressBy(10)


            val TEN_MEGABYTE: Long = 10 * 1024 * 1024
            // Upload to Storage
            val bitmap = (_ivFile.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data  = baos.toByteArray()
            _progressBar.incrementProgressBy(20)

            Log.d("BYTEARRAY SIZE", data.size.toString())
            if (data.size <= TEN_MEGABYTE)
            {
                firestore.collection("tbPosts").document(uuid)
                    .set(post)
                    .addOnSuccessListener {
                        Log.d("Firestore", "Successfully add post" + uuid)
                        _progressBar.incrementProgressBy(20)

                        val storageRef = storage.reference
                        val imageRef = storageRef.child("images/" + uuid)
                        _progressBar.incrementProgressBy(20)


                        var uploadTask = imageRef.putBytes(data)
                        uploadTask
                            .addOnSuccessListener {
                                _progressBar.incrementProgressBy(30)
                                // Toast.makeText(context, "Upload Berhasil", Toast.LENGTH_SHORT).show()
                                MotionToast.createColorToast(requireActivity(), "Success",
                                    "Upload Berhasil",
                                    MotionToastStyle.SUCCESS,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.SHORT_DURATION,
                                    ResourcesCompat.getFont(requireActivity(), R.font.gilroy_light))
                                //  Clear the form
                                _ivFile.setImageResource(0)
                                _etCaption.setText("")
                                _progressBar.setProgress(0)
                                Log.d("Storage", it.metadata.toString())
                            }
                            .addOnFailureListener {
                                // Delete Firestore Posts
                                firestore.collection("tbPosts").document(uuid)
                                    .delete()
                                    .addOnSuccessListener {
                                        // Toast.makeText(context, "Upload Gagal", Toast.LENGTH_SHORT).show()
                                        MotionToast.createColorToast(requireActivity(), "Error",
                                            "Upload Gagal",
                                            MotionToastStyle.ERROR,
                                            MotionToast.GRAVITY_BOTTOM,
                                            MotionToast.SHORT_DURATION,
                                            ResourcesCompat.getFont(requireActivity(), R.font.gilroy_light))
                                        _ivFile.setImageResource(0)
                                        _etCaption.setText("")
                                        _progressBar.setProgress(0)
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(
                                            context,
                                            "Firebase Bermasalah",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        Log.d("Firebase", it.message.toString())
                                    }
                                Log.d("Storage", it.toString())
                            }




                    }
                    .addOnFailureListener {
                        // Toast.makeText(context, "Upload Gagal", Toast.LENGTH_SHORT).show()
                        MotionToast.createColorToast(requireActivity(), "Error",
                            "Upload Gagal",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.SHORT_DURATION,
                            ResourcesCompat.getFont(requireActivity(), R.font.gilroy_light))
                        Log.d("Firestore", it.message.toString())
                    }
            }
            else {
                // Toast.makeText(context, "File terlalu besar ( > 10 MB )", Toast.LENGTH_LONG).show()
                MotionToast.createColorToast(requireActivity(), "Warning",
                    "File terlalu besar ( < 1 MB)",
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.SHORT_DURATION,
                    ResourcesCompat.getFont(requireActivity(), R.font.gilroy_light))
                _progressBar.setProgress(0)
            }

        }
    }

    fun chooseImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startForResult.launch(intent)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Upload.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Upload().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}