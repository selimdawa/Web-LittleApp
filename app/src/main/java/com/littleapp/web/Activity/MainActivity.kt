package com.littleapp.web.Activity

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.littleapp.web.Unit.CLASS
import com.littleapp.web.Unit.VOID
import com.littleapp.web.R
import com.littleapp.web.Unit.DATA
import com.littleapp.web.Unit.THEME
import com.littleapp.web.databinding.ActivityMainBinding
import com.littleapp.web.databinding.ItemWebCardBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val context: Context = this
    private var alertDialog: AlertDialog? = null

    private val callPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(applicationContext, "Permission granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.nameSpace.setText(R.string.web_app)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            callPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
        }

        binding.webSite.setOnClickListener {
            VOID.IntentExtra(context, CLASS.WEB_VIEW, DATA.WEB_NAME, DATA.WEBSITE)
        }
        binding.instagram.setOnClickListener {
            VOID.IntentExtra(context, CLASS.WEB_VIEW, DATA.WEB_NAME, DATA.INSTAGRAM)
        }
        binding.twitter.setOnClickListener {
            VOID.IntentExtra(context, CLASS.WEB_VIEW, DATA.WEB_NAME, DATA.TWITTER)
        }
        binding.facebook.setOnClickListener {
            VOID.IntentExtra(context, CLASS.WEB_VIEW, DATA.WEB_NAME, DATA.FACEBOOK)
        }

        binding.aboutUs.setOnClickListener {
            val dialogBinding = ItemWebCardBinding.inflate(layoutInflater)
            val dialogBuilder = AlertDialog.Builder(context).setView(dialogBinding.root)

            dialogBinding.aboutApp.visibility = View.VISIBLE
            dialogBinding.contact.visibility = View.GONE
            dialogBinding.aboutUs.visibility = View.VISIBLE
            dialogBinding.aboutUs.text = DATA.aboutUs

            alertDialog = dialogBuilder.create().apply {
                dialogBinding.close.setOnClickListener { dismiss() }
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                show()
            }
        }

        binding.support.setOnClickListener {
            val dialogBinding = ItemWebCardBinding.inflate(layoutInflater)
            val dialogBuilder = AlertDialog.Builder(context).setView(dialogBinding.root)

            dialogBinding.aboutApp.visibility = View.GONE
            dialogBinding.contact.visibility = View.VISIBLE
            dialogBinding.aboutUs.visibility = View.GONE

            alertDialog = dialogBuilder.create().apply {
                dialogBinding.close.setOnClickListener { dismiss() }
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                dialogBinding.email.setOnClickListener {
                    val emailSelectorIntent = Intent(Intent.ACTION_SENDTO).apply {
                        data = "mailto:".toUri()
                    }
                    Intent(Intent.ACTION_SEND).apply {
                        putExtra(Intent.EXTRA_EMAIL, arrayOf(DATA.myEmail))
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                        selector = emailSelectorIntent
                        startActivity(this)
                    }
                }

                dialogBinding.phone.setOnClickListener {
                    Intent(Intent.ACTION_CALL).apply {
                        data = "tel:${DATA.myMobileNumber}".toUri()
                        startActivity(this)
                    }
                }
                show()
            }
        }

        binding.shareApp.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
                putExtra(Intent.EXTRA_TEXT, "Share App with\nhttps://play.google.com/store/apps/details?id=${context.packageName}")
                startActivity(Intent.createChooser(this, "Share link!"))
            }
        }

        binding.rateApp.setOnClickListener {
            val marketUri = "market://details?id=${context.packageName}".toUri()
            val goToMarket = Intent(Intent.ACTION_VIEW, marketUri).apply {
                addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            }
            try {
                startActivity(goToMarket)
            } catch (_: ActivityNotFoundException) {
                startActivity(
                    Intent(Intent.ACTION_VIEW, "https://play.google.com/store/apps/details?id=${context.packageName}".toUri())
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        alertDialog?.dismiss()
        alertDialog = null
    }
}