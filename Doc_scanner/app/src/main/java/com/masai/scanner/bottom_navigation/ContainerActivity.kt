package com.masai.scanner.bottom_navigation


import android.Manifest
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.masai.scanner.R
import com.masai.scanner.home_tabs.MyDocumentsFragment
import com.masai.scanner.side_drawer.SettingFragment
import com.pdftron.pdf.*
import com.pdftron.pdf.config.ViewerConfig
import com.pdftron.pdf.controls.DocumentActivity
import com.pdftron.sdf.SDFDoc
import com.scanlibrary.ScanConstants
import com.scanlibrary.ScannerContract
import com.scanlibrary.Utils
import kotlinx.android.synthetic.main.activity_container.*
import kotlinx.android.synthetic.main.navigation_header.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.*


class ContainerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var fragmentManager: FragmentManager
    private lateinit var nav: NavigationView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var headerView: View
    var navigationFragment: Fragment? = null


//    private val PERMISSION_CODE = 1000
//    private val IMAGE_CAPTURE_CODE = 1001

//    var imageUri: Uri? = null
//    var lv_pdf: ListView? = null

//       companion object{
//           @JvmField
//           var filelist = ArrayList<File>()
//          }

//    var obj_adapter: PDFAdapter? = null
//    var dir: File? = null

    //

    private lateinit var button: FloatingActionButton
//    private lateinit var progressBar: ProgressBar
//    private lateinit var progressText: TextView

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        for (hasPermission in permissions.values) {
            if (!hasPermission) {
                Toast.makeText(this, "Missing Required Permissions", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    fun androidRectToPdfRect(
        androidRect: android.graphics.Rect,
        ratio: Double,
        imgHeight: Double
    ): Rect {
        return Rect(
            (androidRect.left.toDouble() * ratio),
            ((imgHeight - androidRect.bottom.toDouble()) * ratio),
            (androidRect.right.toDouble() * ratio),
            ((imgHeight - androidRect.top.toDouble()) * ratio)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
        // Add callback to handle returned image from scanner
        val scannerLauncher = registerForActivityResult(ScannerContract()) { uri ->
            if (uri != null) {
                // Obtain the bitmap and save as a local image file
                var bitmap: Bitmap? = null
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                val imgHeight = bitmap.height.toDouble()
                val imgWidth = bitmap.width.toDouble()
                contentResolver.delete(uri!!, null, null)

                // Save bitmap to local cache as image then upload for processing
                val localJpeg = Utils.saveBitmapAsJpeg(bitmap, filesDir)
                val image = InputImage.fromFilePath(this, Uri.fromFile(localJpeg))

                // showProgress()

                // Process image using ML Kit
                processOCR(imgWidth, imgHeight, image, localJpeg)
            }
        }
//        fab.setOnClickListener {
//          //check()
//        }
        //init()
        button = findViewById(R.id.fab)
//        progressBar = findViewById(R.id.loading)
//        progressText = findViewById(R.id.progress_text)
        button.setOnClickListener {
            // Launch the scanner activity
            scannerLauncher.launch(ScanConstants.OPEN_CAMERA)
        }

        // Check for permission before proceeding
        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.INTERNET
            )
        )
        loadFragment(Fragment())
        fragmentManager = supportFragmentManager

        bottomNavigationWorking()

        toolbar = findViewById(R.id.toolbar0)
        setSupportActionBar(toolbar)

        nav = findViewById(R.id.navMenu)
        nav.setNavigationItemSelectedListener(this)


        drawerLayout = findViewById(R.id.drawer)

        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        headerView = nav.getHeaderView(0)

        val signin = headerView.findViewById<TextView>(R.id.signIn)
        //write code of firebase to set username to sign in user
        signin.text = "ABCDEF"

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun bottomNavigationWorking() {
        val bottomNavigationView =
            findViewById<View>(R.id.bottomNavigationView) as BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home_navigation -> {
                    navigationFragment = HomeFragment()
                }
                R.id.pdfTools_navigation -> {
                    navigationFragment = PdfToolsFragment()
                }
            }
            navigationFragment?.let { loadFragment(it) }
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun loadFragment(navigationFragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(
            R.id.flContainer,
            navigationFragment
        ).commit()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting -> navigationFragment = SettingFragment()
            R.id.shareThisApp -> Toast.makeText(
                this,
                "share app frgment need to added",
                Toast.LENGTH_SHORT
            ).show()
            //all content should add here
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        navigationFragment?.let { loadFragment(it) }
        return true
    }


    //need to set listener
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.app_bar_menu, menu)
//        val menuItem = menu?.findItem(R.id.searchBar)
//        val searchView = menuItem?.actionView as SearchView
//
//
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String): Boolean {
//                val docFrag = MyDocumentsFragment()
//                docFrag.pdfAdapter?.filter?.filter(newText)
//                return false
//            }
//        })
//
//
//        return super.onCreateOptionsMenu(menu)
//    }


    // fun check() {
//        //first we check permission
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.CAMERA) ==
//                PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
//                PackageManager.PERMISSION_DENIED
//            ) {
//                val permission =
//                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                requestPermissions(
//                    permission,PERMISSION_CODE
//
//                )
//            } else {
//                OpenCamera()
//            }
//        } else {
//            OpenCamera()
//        }
//    }
//    private fun OpenCamera() {
//        val values = ContentValues()
//        values.put(MediaStore.Images.Media.TITLE, "New Image")
//        values.put(MediaStore.Images.Media.DESCRIPTION, "from the camera")
//        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
//        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
//        startActivityForResult(
//            cameraIntent,
//            IMAGE_CAPTURE_CODE
//        )
//    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == RESULT_OK) {
//            val i = Intent(this@ContainerActivity, ScanActivity::class.java)
//            i.data = imageUri
//            startActivity(i)
//
//            //so we send image uri to next activity w
//        }
//    }
//    private fun init() {
//        lv_pdf = findViewById(R.id.recyclerViewForFile)
//
//        //path to the directory where all pdf are stored
//        dir = File(Environment.getExternalStorageDirectory().absolutePath, "CamScannerCloneStorage")
//        getfile(dir!!)
//        //so getfile function is complete
//        //now we need to set adapter to listview
//        obj_adapter =
//            PDFAdapter(applicationContext, filelist)
//        lv_pdf?.adapter = obj_adapter
//
//        //so here we need to implement on click listener so that when user click on any pdf then that pdf is open
//        lv_pdf?.onItemClickListener =
//            OnItemClickListener { adapterView, view, i, l ->
//                val intent = Intent(applicationContext, PdfActivity::class.java)
//                intent.putExtra("position", i)
//                startActivity(intent)
//            }
//    }
//    private fun getfile(dir: File): ArrayList<File> {
//        val listFile = dir.listFiles()
//        if (listFile != null && listFile.size > 0) {
//            for (i in listFile.indices) {
//                if (listFile[i].isDirectory) {
//                    getfile(listFile[i])
//                } else {
//                    var booleanpdf = false
//                    //we need only .pdf file
//                    if (listFile[i].name.endsWith(".pdf")) {
//                        for (j in filelist.indices) {
//                            if (filelist.get(j)
//                                    .getName() == listFile[i].name
//                            ) {
//                                booleanpdf = true
//                            }
//                        }
//                        if (booleanpdf) {
//                            booleanpdf = false
//                        } else {
//                           filelist.add(listFile[i])
//                        }
//                    }
//                }
//            }
//        }
//        return filelist
//    }
    private fun processOCR(
        imgWidth: Double,
        imgHeight: Double,
        image: InputImage,
        localJpeg: File
    ) {
        val result = TextRecognition.getClient().process(image)
            .addOnSuccessListener { visionText ->

                // Create the PDF containing the recognized text
                val outputPath = createPDF(imgWidth, imgHeight, localJpeg, visionText)

                // Open the document in the viewer
                val config =
                    ViewerConfig.Builder().openUrlCachePath(cacheDir.absolutePath).build()
                DocumentActivity.openDocument(
                    this@ContainerActivity,
                    Uri.fromFile(outputPath),
                    config
                )
                //readPdf(outputPath)
                // hideProgress()
                //startActivity(Intent(this,ContainerActivity::class.java))
                Toast.makeText(this, outputPath.toString(), Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener { e ->
                // hideProgress()
                Toast.makeText(this, "Could not recognize text", Toast.LENGTH_SHORT).show()
            }
    }

    fun createPDF(
        imgWidth: Double,
        imgHeight: Double,
        localJpeg: File,
        visionText: com.google.mlkit.vision.text.Text
    ): File {

        val doc = PDFDoc()
        val outputFile = File(
            this.filesDir, com.pdftron.pdf.utils.Utils.getFileNameNotInUse(
                "scanned_doc_output.pdf"
            )
        )

        // First convert the image to a PDF Doc
        Convert.toPdf(doc, localJpeg.absolutePath)

        val page = doc.getPage(1) // currently this sample only supports 1 page
        val ratio = page.pageWidth / imgWidth;

        // We will need to generate a JSON containing the text data, which will be used
        // to insert the text information into the PDF document
        val jsonWords = JSONArray()
        for (block in visionText.textBlocks) {
            for (line in block.lines) {
                for (element in line.elements) {
                    val elementText = element.text
                    val elementFrame = element.boundingBox

                    val pdfRect =
                        androidRectToPdfRect(elementFrame!!, ratio, imgHeight)
                    pdfRect.normalize()

                    val word = JSONObject()
                    word.put("font-size", (pdfRect.y2 - pdfRect.y1).toInt())
                    word.put("length", (pdfRect.x2 - pdfRect.x1).toInt())
                    word.put("text", elementText)
                    word.put("orientation", "U")
                    word.put("x", pdfRect.x1.toInt())
                    word.put("y", pdfRect.y1.toInt())
                    jsonWords.put(word)
                }
            }
        }

        val jsonObj = JSONObject()
        val jsonPages = JSONArray()

        val jsonPage = JSONObject()
        jsonPage.put("Word", jsonWords)
        jsonPage.put("num", 1) // Only supports one page
        jsonPage.put("dpi", 96)
        jsonPage.put("origin", "BottomLeft")

        jsonPages.put(jsonPage)
        jsonObj.put("Page", jsonPages)

        // Insert the text into the document
        OCRModule.applyOCRJsonToPDF(doc, jsonObj.toString());
        doc.save(outputFile.absolutePath, SDFDoc.SaveMode.LINEARIZED, null)
        return outputFile
    }

//    private fun showProgress() {
//        progressBar.visibility = View.VISIBLE
//        progressText.visibility = View.VISIBLE
//        button.visibility = View.GONE
//    }

    //    private fun hideProgress() {
//        progressBar.visibility = View.GONE
//        progressText.visibility = View.GONE
//        button.visibility = View.VISIBLE
//    }
    private fun readPdf(file: File) {
//        val path =
//            File(string)
//        val pdfView = findViewById<PDFView>(R.id.pdfView)
//        pdfView.fromFile(file).load()
    }
}