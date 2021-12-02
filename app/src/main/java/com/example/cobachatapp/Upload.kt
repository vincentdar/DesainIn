package com.example.cobachatapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import org.w3c.dom.Text

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
    private val pickImage = 100
    private var imageUri: Uri? = null
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
        }
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
        return inflater.inflate(R.layout.fragment_upload, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val _etCaption = view.findViewById<EditText>(R.id.etCaption)
        val _btnUpload = view.findViewById<Button>(R.id.btnUpload)
        val _tvFrUpload = view.findViewById<TextView>(R.id.tvFrUpload)
        val _btnChooseFile = view.findViewById<Button>(R.id.btnChooseFile)
        val _ivFile = view.findViewById<ImageView>(R.id.ivFile)
        val _progressBar = view.findViewById<ProgressBar>(R.id.progressBar)

        _btnUpload.setOnClickListener {
            var str = _etCaption.text.toString()
            _tvFrUpload.setText(str)
        }

        _btnChooseFile.setOnClickListener {
            chooseImage()
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