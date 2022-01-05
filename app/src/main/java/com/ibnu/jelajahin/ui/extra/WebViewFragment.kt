package com.ibnu.jelajahin.ui.extra

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.ibnu.jelajahin.databinding.FragmentWebViewBinding
import android.webkit.WebChromeClient

class WebViewFragment : Fragment() {

    private var _binding: FragmentWebViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWebViewBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val safeArgs = arguments?.let { WebViewFragmentArgs.fromBundle(it) }
        val webUrl = safeArgs?.url ?: ""

        initiateWebViewSettings()

        binding.webview.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                binding.progressBar.visibility = View.VISIBLE
                binding.progressBar.progress = newProgress
                if (newProgress == 100) {
                    binding.progressBar.visibility = View.GONE
                }
                super.onProgressChanged(view, newProgress)
            }
        }

        binding.webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null) {
                    view?.loadUrl(url)
                }
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.progressBar.visibility = View.GONE
            }
        }
        binding.webview.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        binding.webview.loadUrl(webUrl)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initiateWebViewSettings() {
        val webSetting: WebSettings = binding.webview.settings
        webSetting.javaScriptEnabled = true
        webSetting.allowContentAccess = true
        webSetting.useWideViewPort = true
        webSetting.loadsImagesAutomatically = true
        webSetting.cacheMode = WebSettings.LOAD_NO_CACHE
        webSetting.domStorageEnabled = true
    }

}