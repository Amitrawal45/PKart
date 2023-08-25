package com.amitdroid.pkart.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.amitdroid.pkart.R
import com.amitdroid.pkart.activity.AddressActivity
import com.amitdroid.pkart.activity.CategoryActivity
import com.amitdroid.pkart.adapter.CartAdapter
import com.amitdroid.pkart.databinding.FragmentCartBinding
import com.amitdroid.pkart.roomdb.AppDatabase
import com.amitdroid.pkart.roomdb.ProductModel


class CartFragment : Fragment() {
   private lateinit var binding: FragmentCartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =FragmentCartBinding.inflate(layoutInflater)
        val preference = requireContext().getSharedPreferences("info",AppCompatActivity.MODE_PRIVATE)
        val editor = preference.edit()
        editor.putBoolean("isCart",false)
        editor.apply()

        val dao =AppDatabase.getInstance(requireContext()).productDao()

        dao.getAllProducts().observe(requireActivity()){
            binding.cartRecycler.adapter = CartAdapter(requireContext(),it)
            totalCost(it)
        }

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun totalCost(data: List<ProductModel>?) {
        var total =0
        for (item in data!!){

            total += item.productSp?.toIntOrNull() ?: 0

        }

        binding.textView8.text ="Total item in Cart is ${data.size}"

        binding.textView9.text ="Total Cost: $total"

        binding.checkOut.setOnClickListener {
            val intent = Intent(context, AddressActivity::class.java)
            intent.putExtra("totalCost",total)
            startActivity(intent)
        }
    }


}