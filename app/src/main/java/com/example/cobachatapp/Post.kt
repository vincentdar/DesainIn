package com.example.cobachatapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Post.newInstance] factory method to
 * create an instance of this fragment.
 */
class Post : Fragment() {
    // TODO: Rename and change types of parameter
    private var param1: String? = null
    private var param2: String? = null
    private var userId: String? = null
    private var userName: String? = null

    // RecyclerView Data
    private var _id = ArrayList<String>()
    private var _username = mutableListOf<String>()
    private var _tanggal = mutableListOf<String>()
    private var _image = arrayListOf<ByteArray>()
    private var _caption = mutableListOf<String>()
    private var _dataPosts = arrayListOf<dcPost>()

    private lateinit var _rvPosts: RecyclerView

    // Firebase Services
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var storage: FirebaseStorage = FirebaseStorage.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            userId = param1
            userName = param2
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _rvPosts = view.findViewById(R.id.rvPosts)

        ReadData()
    }

    fun ReadData() {
        val storageRef = storage.reference
        firestore.collection("tbPosts").get()
            .addOnSuccessListener { result ->
                _username.clear()
                _tanggal.clear()
                _caption.clear()
                _dataPosts.clear()


                for (document in result) {
                    if (document.data.get("userId") == userId) {
                        _id.add(document.id)
                        _username.add(userName.toString())
                        _tanggal.add(document.data.get("tanggal").toString())
                        _caption.add(document.data.get("caption").toString())
                        var b: ByteArray = byteArrayOf(0x0)
                        _image.add(b)
                    }
                }
                var wait = 0
                for(id in _id.indices) {
                    var storageRef = storage.reference
                    var imageRef = storageRef.child("images/" + _id[id])
                    val ONE_MEGABYTE: Long = 1024 * 1024
                    imageRef.getBytes(ONE_MEGABYTE)
                        .addOnSuccessListener {
                            _image.set(id, it)
                            wait += 1
                            if (wait >= _id.size) {
                                DataToDataClass()
                                TampilkanData()
                            }
                        }
                        .addOnFailureListener {
                            Log.d("Storage", "Failure to get image")
                        }
                }


            }
    }

    fun ReadImage(uuid: String, index: Int) {
        var storageRef = storage.reference
        var imageRef = storageRef.child("images/" + uuid)
        val ONE_MEGABYTE: Long = 1024 * 1024
        imageRef.getBytes(ONE_MEGABYTE)
            .addOnSuccessListener {
                _image.set(index, it)
            }
            .addOnFailureListener {
                Log.d("Storage", "Failure to get image")
            }
    }

    fun DataToDataClass() {
        for(position in _username.indices) {
            val data = dcPost(_username[position], _caption[position] ,_tanggal[position])
            _dataPosts.add(data)
        }
    }

    fun HapusData(uuid: String) {
        firestore.collection("tbPosts").document(uuid).delete()
            .addOnSuccessListener {
                var storageRef = storage.reference
                var imageRef = storageRef.child("images/" + uuid)
                imageRef.delete()
                    .addOnSuccessListener {
                        Toast.makeText(context, "Post dihapus", Toast.LENGTH_SHORT).show()
                        ReadData()
                    }
                    .addOnFailureListener {
                        Log.d("Storage", "Failure to delete post")
                    }
            }
            .addOnFailureListener {
                Log.d("Firestore", "Failure to delete post")
            }

    }

    fun TampilkanData() {
        _rvPosts.layoutManager = LinearLayoutManager(context)
        val postsAdapter = PostsAdapter(_dataPosts, _image, _id)
        _rvPosts.adapter = postsAdapter

        postsAdapter.SetOnItemClickCallback(object : PostsAdapter.OnItemClickCallback {
            override fun OnItemDelete(uuid: String) {
                HapusData(uuid)
            }
        })
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Post.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Post().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}