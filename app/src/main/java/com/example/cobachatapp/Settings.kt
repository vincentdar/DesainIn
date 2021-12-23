package com.example.cobachatapp

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.firestore.FirebaseFirestore
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Settings.newInstance] factory method to
 * create an instance of this fragment.
 */
class Settings : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var firestore = FirebaseFirestore.getInstance()
    private var user: User = StaticHolder.get_current_user()
    private lateinit var _btnBecomeDesigner: Button

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
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val _btnUpdatePic = view.findViewById<Button>(R.id.btnUpdatePic)
        _btnBecomeDesigner = view.findViewById<Button>(R.id.btnBecomeDesigner)

        _btnUpdatePic.setOnClickListener {
            val mFragmentManager = parentFragmentManager
            val mfProfilePicture = ProfilePicture()

            mFragmentManager.findFragmentByTag(ProfilePicture::class.java.simpleName)
            mFragmentManager
                .beginTransaction()
                .replace(R.id.frameContainer, mfProfilePicture, ProfilePicture::class.java.simpleName)
                .addToBackStack(null)
                .commit()
        }

        CheckIsDesigner()
        _btnBecomeDesigner.setOnClickListener {
            if (user.desainer == "0") {
                becomeDesigner(true)
            }
            else {
                becomeDesigner(false)
            }
        }

    }

    fun CheckIsDesigner() {
        if (user.desainer == "1") {
            _btnBecomeDesigner.setText("Revert to client")
            _btnBecomeDesigner.setBackgroundColor(Color.parseColor("#808080"))
        }
        else {
            _btnBecomeDesigner.setText("Become a designer")
            _btnBecomeDesigner.setBackgroundColor(Color.parseColor("#FFD523"))
        }
    }

    fun becomeDesigner(desainer: Boolean) {
        if (desainer) {
            firestore.collection("tbUsers").document(user.userId.toString()).update("desainer", 1)
                .addOnSuccessListener {
                    CheckIsDesigner()
                    MotionToast.createColorToast(requireActivity(), "Success",
                        "Berhasil menjadi desainer",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.SHORT_DURATION,
                        ResourcesCompat.getFont(requireActivity(), R.font.gilroy_light))
                }
                .addOnFailureListener {
                    MotionToast.createColorToast(requireActivity(), "Error",
                        "Gagal menjadi desainer",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.SHORT_DURATION,
                        ResourcesCompat.getFont(requireActivity(), R.font.gilroy_light))
                }
        }
        else {
            firestore.collection("tbUsers").document(user.userId.toString()).update("desainer", 0)
                .addOnSuccessListener {
                    CheckIsDesigner()
                    MotionToast.createColorToast(requireActivity(), "Success",
                        "Berhasil menjadi desainer",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.SHORT_DURATION,
                        ResourcesCompat.getFont(requireActivity(), R.font.gilroy_light))
                }
                .addOnFailureListener {
                    MotionToast.createColorToast(requireActivity(), "Error",
                        "Gagal menjadi desainer",
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
         * @return A new instance of fragment Settings.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Settings().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}