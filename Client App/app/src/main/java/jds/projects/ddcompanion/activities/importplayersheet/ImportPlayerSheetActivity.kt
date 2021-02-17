package jds.projects.ddcompanion.activities.importplayersheet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.my_classes.io.files.PlayerSheetFileManager.CODE.ERROR_FILE_NOT_SUPPORTED
import jds.projects.ddcompanion.my_classes.io.files.PlayerSheetFileManager.CODE.ERROR_MANUAL_NOT_PRESENT
import jds.projects.ddcompanion.my_classes.io.files.PlayerSheetFileManager.CODE.SUCCESS
import jds.projects.ddcompanion.my_classes.utils.DayNightActivity
import jds.projects.ddcompanion.my_classes.utils.JDUtils

class ImportPlayerSheetActivity : DayNightActivity() {

    private lateinit var txvImportedFile: TextView
    private lateinit var viewModel: ImportPlayerSheetViewModel
    private lateinit var btnImport: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_import_player_sheet)
        viewModel = ImportPlayerSheetViewModel(applicationContext)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.container,
                    viewModel.importFragment
                )
                .commitNow()
        }

        txvImportedFile = findViewById(R.id.txv_activity_import_player_sheet__selectedfile)
        btnImport = findViewById(R.id.btn_activity_import_player_sheet__import)
        btnImport.setOnClickListener {
            //se l'import si è concluso in modo corretto, importo il playersheet nel db
            if (viewModel.importPlayerSheetInDB()) {
                Toast.makeText(
                    applicationContext,
                    resources.getString(R.string.importa_scheda_import_success),
                    Toast.LENGTH_SHORT
                )
                    .show()
                setActivityResult(viewModel.importedPlayerSheet.playerSheetID)
            }
        }
        val btnSelectFile = findViewById<Button>(R.id.btn_activity_import_player_sheet__selectfile)
        btnSelectFile.setOnClickListener {
            //faccio partire un intent x scegliere un file di testo da importare
            val chooseFile = Intent(Intent.ACTION_GET_CONTENT)
            chooseFile.addCategory(Intent.CATEGORY_OPENABLE)
            chooseFile.type = "text/plain"
            startActivityForResult(
                Intent.createChooser(
                    chooseFile,
                    resources.getString(R.string.importa_scheda_scegli_file)
                ),
                JDUtils.REQUESTS.PICK_FILE_RESULT_CODE
            )
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun showPlayerSheetInfo() {
        //attivo il pulsante di import
        if (!btnImport.isEnabled)
            btnImport.isEnabled = true
        //aggiorno i dati con quelli nuovi
        viewModel.loadPlayerSheet()
    }

    private fun setActivityResult(resultID: Int) {
        if (resultID > 0) {
            val intent = Intent()
            intent.putExtra(JDUtils.BUNDLES.PLAYER_SHEET_ID, resultID)
            setResult(RESULT_OK, intent)
            finish()
        } else {
            setResult(RESULT_CANCELED)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        setActivityResult(0)
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == JDUtils.REQUESTS.PICK_FILE_RESULT_CODE && resultCode == Activity.RESULT_OK && data != null) {
            //Log.d("FileName is ", filename)
            when (viewModel.loadPlayerSheetFromUri(data.data!!)) {
                SUCCESS -> {
                    //imposto il testo della textview con il nome del file preso
                    txvImportedFile.text = data.data!!.lastPathSegment
                    showPlayerSheetInfo()
                }
                ERROR_FILE_NOT_SUPPORTED -> {
                    Toast.makeText(
                        applicationContext,
                        resources.getString(R.string.importa_scheda_import_not_supported),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                ERROR_MANUAL_NOT_PRESENT -> {
                    Toast.makeText(
                        applicationContext,
                        resources.getString(
                            R.string.importa_scheda_import_not_available,
                            viewModel.manualIDNotFound
                        ),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}