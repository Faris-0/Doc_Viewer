package com.yuuna.docviewer

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.cherry.lib.doc.DocViewerActivity
import com.cherry.lib.doc.bean.DocSourceType
import com.yuuna.docviewer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestStoragePermission()
        if (intent.data != null) DocViewerActivity.launchDocViewer(this, DocSourceType.URI, intent.data.toString())
        binding.openDoc.setOnClickListener { v -> resultLauncher.launch(Intent(Intent.ACTION_GET_CONTENT).setType("*/*")) }
    }

    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
            startActivity(Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, Uri.parse(java.lang.String.format("package:%s", packageName))))
        }
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { uri ->
        if (uri.data != null) DocViewerActivity.launchDocViewer(this, DocSourceType.URI, uri.data?.data.toString())
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        DocViewerActivity.launchDocViewer(this, DocSourceType.URI, intent?.data.toString())
    }
}