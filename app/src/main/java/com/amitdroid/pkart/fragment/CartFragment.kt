package com.amitdroid.pkart.fragment


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.amitdroid.pkart.activity.AddressActivity
import com.amitdroid.pkart.databinding.FragmentCartBinding
import com.amitdroid.pkart.roomdb.AppDatabase
import com.amitdroid.pkart.roomdb.ProductModel
import com.amitdroid.pkart.adapter.CartAdapter

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCartBinding.inflate(inflater, container, false)
        val preference = requireContext().getSharedPreferences("info", AppCompatActivity.MODE_PRIVATE)
        val editor = preference.edit()
        editor.putBoolean("isCart", false)
        editor.apply()

        val dao = AppDatabase.getInstance(requireContext()).productDao()

        dao.getAllProducts().observe(viewLifecycleOwner) {
            binding.cartRecycler.adapter = CartAdapter(requireContext(), it)
            totalCost(it)
        }

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun totalCost(data: List<ProductModel>?) {
        var total = 0
        for (item in data!!) {
            total += item.productSp?.toIntOrNull() ?: 0
        }

        binding.textView8.text = "Total items in Cart: ${data.size ?: 0}"
        binding.textView9.text = "Total Cost: $total"

        binding.checkOut.setOnClickListener {
            val intent = Intent(context, AddressActivity::class.java)
            intent.putExtra("totalCost", total)
            startActivity(intent)
        }
    }
}
