package com.sullivan.signear.ui_reservation.ui.mypage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sullivan.common.ui_common.navigator.LoginNavigator
import com.sullivan.sigenear.ui_reservation.BuildConfig
import com.sullivan.sigenear.ui_reservation.R
import com.sullivan.sigenear.ui_reservation.databinding.ItemMypageBinding

class MyPageListAdapter(
    private val itemList: List<String>,
    private val loginNavigator: LoginNavigator,
    private val activity: Activity,
    private val clearAccessToken: () -> Unit
) :
    RecyclerView.Adapter<MyPageListAdapter.MyPageListViewHolder>() {

    private lateinit var binding: ItemMypageBinding

    inner class MyPageListViewHolder(private val binding: ItemMypageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun binding(item: String) {
            binding.apply {
                tvTitle.text = item
                rlMypage.setOnClickListener {
                    when (item) {
                        itemList[0] -> {
                            it.findNavController()
                                .navigate(R.id.action_myPageFragment_to_previousReservationFragment)
                        }
                        itemList[1] -> sendEmail(it.context)
                        itemList[2] -> {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse(RULE_LINK1)
                            it.context.startActivity(intent)
                        }
                        itemList[3] -> {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse(RULE_LINK2)
                            it.context.startActivity(intent)
                        }
                        itemList[4] -> showDialog(it.context)
                    }
                }
            }
        }

        private fun sendEmail(context: Context) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf("sullivan_developer@signear.com")
            )
            intent.putExtra(
                Intent.EXTRA_SUBJECT,
                R.string.fragment_my_page_email_title
            )
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "??? ?????? (AppVersion): ${BuildConfig.VERSION_NAME}\n????????? (Device):\n??????????????? OS (Android OS): ${Build.VERSION.RELEASE + ".0"}\n?????? (Content):\n"
            )
            context.startActivity(intent)
        }

        private fun showDialog(context: Context) {
            val dialog = MaterialAlertDialogBuilder(
                context, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog
            )
                .setTitle(R.string.fragment_my_page_dialog_logout_title)
                .setMessage(R.string.fragment_my_page_dialog_logout_body)
                .setPositiveButton(R.string.fragment_my_page_dialog_logout_positive_btn_title) { dialog, _ ->
                    clearAccessToken()
                    loginNavigator.openLogin(activity)
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.fragment_my_page_dialog_logout_negative_btn_title) { dialog, _ ->
                    dialog.dismiss()
                }
                .setCancelable(false)
                .create()

            dialog.show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = ItemMypageBinding.inflate(layoutInflater)
        return MyPageListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyPageListViewHolder, position: Int) {
        holder.binding(itemList[position])
    }

    override fun getItemCount() = itemList.size

    companion object {
        private const val RULE_LINK1 = "https://www.notion.so/ab2186a351444486bacbc7c3771038ae"
        private const val RULE_LINK2 = "https://www.notion.so/720c23a2a2b142c3b6c4cabc9f1bea87"
    }
}