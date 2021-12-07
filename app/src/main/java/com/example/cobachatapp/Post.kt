package com.example.cobachatapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isGone
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
    private var user: User? = null
    private var authenticated: Boolean? = null


    // RecyclerView Data
    private var _id = ArrayList<String>()
    private var _username = mutableListOf<String>()
    private var _tanggal = mutableListOf<String>()
    private var _image = arrayListOf<ByteArray>()
    private var _caption = mutableListOf<String>()
    private var _dataPosts = arrayListOf<dcPost>()

    private lateinit var _rvPosts: RecyclerView
    private lateinit var _progressBarCircular: ProgressBar
    private lateinit var _tvPostEmpty: TextView

    // Firebase Services
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var storage: FirebaseStorage = FirebaseStorage.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getParcelable<User>(ARG_PARAM1)
            authenticated = it.getBoolean(ARG_PARAM2)

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
        _progressBarCircular = view.findViewById(R.id.progressBarCircular)
        _tvPostEmpty = view.findViewById(R.id.tvPostEmpty)
        _tvPostEmpty.isGone = true


        ReadData()
    }

    fun ReadData() {
        Log.d("Firestore", "Reading data")
        firestore.collection("tbPosts").whereEqualTo("userId", user?.userId).get()
            .addOnSuccessListener { result ->
                if (result.size() == 0) {
                    _progressBarCircular.isGone = true
                    _tvPostEmpty.isGone = false
                    return@addOnSuccessListener
                }
                _id.clear()
                _username.clear()
                _tanggal.clear()
                _caption.clear()
                _dataPosts.clear()


                for (document in result) {
                    _id.add(document.id)
                    _username.add(user?.userName.toString())
                    _tanggal.add(document.data.get("tanggal").toString())
                    _caption.add(document.data.get("caption").toString())
                    var b: ByteArray = byteArrayOf(0x0)
                    _image.add(b)
                }
                Log.d("Storage", "Reading images")
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
                                _progressBarCircular.isGone = true
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


    fun DataToDataClass() {
        for(position in _username.indices) {
            val data = dcPost(_username[position], _caption[position] ,_tanggal[position])
            _dataPosts.add(data)
        }
    }

    fun HapusData(uuid: String) {
        var storageRef = storage.reference
        var imageRef = storageRef.child("images/" + uuid)
        imageRef.delete()
            .addOnSuccessListener {
                firestore.collection("tbPosts").document(uuid).delete()
                    .addOnSuccessListener {
                        Toast.makeText(context, "Post dihapus", Toast.LENGTH_SHORT).show()
                        ReadData()
                    }
                    .addOnFailureListener {
                        Log.d("Firestore", "Failure to delete post")
                    }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Post Gagal dihapus", Toast.LENGTH_SHORT).show()
                Log.d("Storage", "Failure to delete post")
            }



    }

    fun TampilkanData() {
        _rvPosts.layoutManager = LinearLayoutManager(context)
        val postsAdapter = PostsAdapter(_dataPosts, _image, _id, authenticated!!)
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
        fun newInstance(param1: User, param2: Boolean) =
            Post().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, param1)
                    putBoolean(ARG_PARAM2, param2)
                }
            }
    }
}

