package com.namnguyen.cryptocurrency.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.SimpleItemAnimator
import com.namnguyen.cryptocurrency.R
import com.namnguyen.cryptocurrency.databinding.ActivityMainBinding
import com.namnguyen.cryptocurrency.ext.connectivity.ConnectivityProvider
import com.namnguyen.cryptocurrency.ext.connectivity.ConnectivityStateListener
import com.namnguyen.cryptocurrency.ext.connectivity.NetworkState
import com.namnguyen.cryptocurrency.ext.hideSoftKeyboard
import com.namnguyen.cryptocurrency.ext.showSoftKeyboard
import com.namnguyen.cryptocurrency.main.adapter.CurrencyListAdapter
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

const val DEFAULT_INTERVAL_TIME = 30000L

class MainActivity : AppCompatActivity(), ConnectivityStateListener {

    private lateinit var mBinding: ActivityMainBinding
    private var mAdapter: CurrencyListAdapter? = CurrencyListAdapter()
    private val mMainViewModel: MainViewModel by viewModel()
    private lateinit var textWatcher: TextWatcher
    private var countDownTimer: CountDownTimer? = null

    private val connectivityProvider by lazy { ConnectivityProvider.createProvider(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initUI()
        initEvents()
        observable()
    }

    private fun initUI() = with(mBinding) {
        mAdapter = CurrencyListAdapter()
        rvCurrency.apply {
            adapter = mAdapter
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initEvents() = with(mBinding) {
        textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mMainViewModel.filterCurrencyByName(s?.toString() ?: "")
            }
        }

        swipeRefresh.run {
            setOnRefreshListener {
                edtCurrency.text?.clear()
                fetchData()
                isRefreshing = false
            }
        }

        rvCurrency.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                view.hideSoftKeyboard()
            }
            false
        }
    }

    private fun observable() = with(mMainViewModel) {
        listCryptoCurrencyLive.observe(this@MainActivity) {
            fetchData()
            mAdapter?.submitList(it)
            updateEmptyView(it.isNotEmpty(), mBinding.edtCurrency.text.isNullOrBlank())
        }

        listCryptoCurrencyFilterLive.observe(this@MainActivity) {
            mAdapter?.submitList(it)
            updateEmptyView(it.isNotEmpty(), mBinding.edtCurrency.text.isNullOrBlank())
        }

        isOnLineLive.observe(this@MainActivity) {
            it ?: return@observe
            if (it) {
                fetchData()
            }
            swipeRefresh.isInvisible = it.not()
            noInternetLay.isInvisible = it
        }

        messageData.observe(this@MainActivity) {
            Toast.makeText(this@MainActivity, it, LENGTH_LONG).show()
        }

        showProgressbar.observe(this@MainActivity) { isVisible ->
            progressBar.visibility = if (isVisible) VISIBLE else GONE
        }
    }

    private fun updateEmptyView(isHideEmptyView: Boolean, textEmpty: Boolean) = mBinding.run {
        rvCurrency.isVisible = isHideEmptyView
        tvEmpty.isVisible = isHideEmptyView.not() && textEmpty.not()
    }

    private fun fetchData() {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(DEFAULT_INTERVAL_TIME, 1000) {
            override fun onFinish() {
                mMainViewModel.getListCryptoCurrency()
                fetchData()
            }

            override fun onTick(millisUntilFinished: Long) {
            }
        }
        countDownTimer?.start()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ${mMainViewModel.isOnLine}")
        if (mMainViewModel.isOnLine) fetchData()
        mBinding.edtCurrency.apply {
            addTextChangedListener(textWatcher)
            requestFocus()
            showSoftKeyboard()
        }
    }

    override fun onPause() {
        mBinding.edtCurrency.apply {
            removeTextChangedListener(textWatcher)
            clearFocus()
            hideSoftKeyboard()
        }
        super.onPause()
    }


    override fun onDestroy() {
        mAdapter = null
        countDownTimer?.cancel()
        super.onDestroy()
    }

    override fun onStart() {
        super.onStart()
        connectivityProvider.addListener(this)
    }

    override fun onStop() {
        connectivityProvider.removeListener(this)
        super.onStop()
    }

    @SuppressLint("RestrictedApi")
    override fun onConnectivityStateChange(state: NetworkState) = with(mMainViewModel) {
        val isConnect = state == NetworkState.Connected
        if (isOnLine != isConnect) {
            isOnLine = isConnect
        }
    }

    companion object {
        private val TAG = MainActivity::class.java.name
    }
}