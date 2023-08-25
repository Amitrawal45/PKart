package com.amitdroid.pkart.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.amitdroid.pkart.R
import com.amitdroid.pkart.adapter.CategoryAdapter
import com.amitdroid.pkart.adapter.ProductAdapter
import com.amitdroid.pkart.databinding.FragmentHomeBinding
import com.amitdroid.pkart.model.AddProductModel
import com.amitdroid.pkart.model.CategoryModel
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val preferences = requireContext().getSharedPreferences("info",AppCompatActivity.MODE_PRIVATE)

        if(preferences.getBoolean("isCart",false))

            findNavController().navigate(R.id.action_homeFragment_to_cartFragment)
        binding = FragmentHomeBinding.inflate(layoutInflater)
        getCategories()
        getSliderImage()
        getProducts()
        return binding.root
    }

    private fun getSliderImage() {
        Firebase.firestore.collection("Slider").document("item").get()
            .addOnSuccessListener{
                Glide.with(requireContext()).load(it.get("img")).into(binding.sliderImage)

            }


    }


    private fun getProducts() {
        val list = ArrayList<AddProductModel>()
        Firebase.firestore.collection("products")
            .get().addOnSuccessListener {
                list.clear()
                for(doc in it.documents){

                    val data = doc.toObject(AddProductModel::class.java)
                    list.add(data!!)
                }
                binding.productRecycler.adapter = ProductAdapter(requireContext(),list)
            }
    }



    private fun getCategories() {
        val list = ArrayList<CategoryModel>()
        Firebase.firestore.collection("categories")
            .get().addOnSuccessListener {
                list.clear()
                for(doc in it.documents){

                    val data = doc.toObject(CategoryModel::class.java)
                    list.add(data!!)
                }
                binding.categoryRecycler.adapter = CategoryAdapter(requireContext(),list)
            }
    }


}

