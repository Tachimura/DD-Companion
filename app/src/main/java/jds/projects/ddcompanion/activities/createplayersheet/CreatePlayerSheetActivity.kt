package jds.projects.ddcompanion.activities.createplayersheet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.my_classes.adapters.FixedSectionsPagerAdapter
import jds.projects.ddcompanion.my_classes.utils.DayNightActivity
import jds.projects.ddcompanion.my_classes.utils.JDPreferences
import jds.projects.ddcompanion.my_classes.utils.JDUtils

class CreatePlayerSheetActivity : DayNightActivity() {
    private lateinit var viewModel: CreatePlayerSheetActivityViewModel
    private lateinit var viewPager: ViewPager2
    private lateinit var btnNext: Button
    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_player_sheet)
        //inizializzo le view
        btnBack = findViewById(R.id.btn_activity_create_player_sheet__back)
        btnNext = findViewById(R.id.btn_activity_create_player_sheet__next)
        //inizializzo il modello dei dati
        viewModel = CreatePlayerSheetActivityViewModel(
            baseContext
        )
        //inizializzo il viewpager
        viewPager = findViewById(R.id.viewpager2_activity_create_player_sheet__pager)
        val tabLayout = findViewById<TabLayout>(R.id.tablayout_activity_create_player_sheet__tabs)
        viewPager.adapter =
            FixedSectionsPagerAdapter(
                this,
                viewModel.getFragments().toTypedArray()
            )
        viewPager.offscreenPageLimit = viewModel.getCount()
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = viewModel.getTabTitle(position)
        }.attach()
        //disattivo lo swipe, si può andare avanti/indietro solo se i pulsanti mi permettono (alcune schermate dipendono da altre)
        viewPager.isUserInputEnabled = false
        //disabilito anche il touch sul tab
        tabLayout?.touchables?.forEach { it.isEnabled = false }
        //imposto la navigation con i due button
        btnNext.setOnClickListener {
            goNext()
        }
        btnBack.setOnClickListener {
            goBack()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun goNext() {
        with(viewModel) {
            val position = getPosition()
            //preparo l'aggiornamento dei dati del prossimo fragment se è tutto apposto
            if (updateView(position + 1)) {
                //se sono all'inizio sblocco il btnBack
                when (position) {
                    0 -> btnBack.isEnabled = true
                    //se sono al penultimo cambio il testo del button next
                    getCount() - 2 -> btnNext.text = resources.getString(R.string.btn_end)
                    //se sono alla fine controllo la scheda
                    getCount() - 1 -> completePlayerSheet()
                    //se l'utente non ha dimenticato nulla posso andare a destra
                }
                //se l'utente non ha dimenticato nulla posso andare a destra
                updatePagerGUI(position, 1)
            }
        }
    }

    private fun goBack() {
        with(viewModel) {
            val position = getPosition()
            if (updateView(position - 1)) {
                when (position) {
                    //se devo tornare alla prima pagina mi disattivo
                    1 -> btnBack.isEnabled = false
                    //se sono alla fine e torno indietro rimetto a posto il btnNext
                    getCount() - 1 -> btnNext.text = resources.getString(R.string.btn_next)
                }
                //se non ci son problemi vado a sinistra
                updatePagerGUI(position, -1)
            }
        }
    }


    private fun updatePagerGUI(value: Int, increase: Int) {
        viewPager.setCurrentItem(value + increase, true)
    }

    /**
     * funzione che controlla se la scheda è a posto, in caso la salva, altrimenti da errore
     */
    private fun completePlayerSheet() {
        with(viewModel) {
            val result = createPlayerSheet()
            if (result > 0) {
                val language = JDPreferences.getInstance(baseContext).language
                Toast.makeText(
                    this@CreatePlayerSheetActivity,
                    resources.getString(
                        R.string.created_player_sheet, playerName,
                        ddRace.names.getTextWithLanguageID(language.id),
                        ddClass.names.getTextWithLanguageID(language.id)
                    ),
                    Toast.LENGTH_LONG
                ).show()
                val intent = Intent()
                intent.putExtra(JDUtils.BUNDLES.PLAYER_SHEET_ID, result)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra(JDUtils.BUNDLES.PLAYER_SHEET_ID, 0)
        setResult(Activity.RESULT_CANCELED, intent)
        finish()
    }
}