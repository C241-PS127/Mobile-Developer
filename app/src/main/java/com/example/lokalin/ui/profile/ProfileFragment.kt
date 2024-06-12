package com.example.lokalin.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lokalin.ViewModelFactory
import com.example.lokalin.databinding.FragmentProfileBinding
import com.example.lokalin.ui.home.ExploreAdapter
import com.example.lokalin.ui.home.ZoomOutPageTransformer
import com.example.response.SliderModel2
import com.example.utils.ResultState

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (user.isLogin) {
                setupAction(user.token)
                //binding.btnLogout.visibility = View.VISIBLE
            }
        }

        val adapter = ExploreAdapter()
        binding.rvExplore.adapter = adapter
        binding.rvExplore.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        viewModel.products.observe(viewLifecycleOwner) { stories ->
            adapter.submitList(stories)
        }

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                // Tampilkan indikator loading
                binding.progressbar2.visibility = View.VISIBLE
            } else {
                // Sembunyikan indikator loading
                binding.progressbar2.visibility = View.GONE
            }
        })

        initBanner()

        return root
    }

    private fun initBanner() {
        binding.progressbar.visibility = View.VISIBLE
        viewModel.banners.observe(viewLifecycleOwner, Observer { items ->
            if (_binding != null) {  // Ensure binding is not null
                banners(items)
                binding.progressbar.visibility = View.GONE
            }
        })
        viewModel.loadBanners()
    }

    private fun banners(images: List<SliderModel2>) {
        binding.viewpagerSlider.adapter = SliderAdapter2(images, binding.viewpagerSlider)
        binding.viewpagerSlider.clipToPadding = false
        binding.viewpagerSlider.clipChildren = true
        binding.viewpagerSlider.offscreenPageLimit = 2
        binding.viewpagerSlider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        binding.viewpagerSlider.setPageTransformer(ZoomOutPageTransformer())

        if (images.size > 1) {
            binding.indicatorDots.visibility = View.VISIBLE
            binding.indicatorDots.attachTo(binding.viewpagerSlider)
        }
    }

    private fun setupAction(token: String) {
        viewModel.getProfile(token).observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    is ResultState.Loading -> {
                        //showLoading(true)
                    }

                    is ResultState.Success -> {
                        binding.apply {
                            tvName.text = it.data[0].fullName
                            emailInput.text = it.data[0].email
                            phoneInput.text = it.data[0].phone
                            addressInput.text = it.data[0].address
                        }
                    }

                    is ResultState.Error -> {
                        //showLoading(false)
                        // showToast(it.error)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
