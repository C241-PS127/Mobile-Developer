package com.example.lokalin.ui.detailproduct

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.lokalin.ViewModelFactory
import com.example.lokalin.databinding.FragmentDetailProductBinding
import com.example.response.Product
import java.text.NumberFormat
import java.util.Locale

class DetailProductFragment : Fragment() {

    private var _binding: FragmentDetailProductBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailProductBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val args: DetailProductFragmentArgs by navArgs()
        val productId = args.ProductId

        viewModel.fetchProductDetail(productId)

        viewModel.productDetail.observe(viewLifecycleOwner) {
            setDetail(it)
        }

        return root
    }

    private fun setDetail(product: Product) {
        binding.apply {
            tvProductName.text = product.productName
            tvDescription.text = product.productDescription
            tvPrice.text = formatRupiah(product.unitPrice.toInt())

        }
    }

    private fun formatRupiah(amount: Int): String {
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        return formatRupiah.format(amount)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}