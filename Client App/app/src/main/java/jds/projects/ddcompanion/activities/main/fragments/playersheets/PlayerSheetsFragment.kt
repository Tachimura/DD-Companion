package jds.projects.ddcompanion.activities.main.fragments.playersheets

import android.animation.Animator
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.activities.createplayersheet.CreatePlayerSheetActivity
import jds.projects.ddcompanion.activities.importplayersheet.ImportPlayerSheetActivity
import jds.projects.ddcompanion.activities.levelupplayersheet.LevelUpPlayerSheetActivity
import jds.projects.ddcompanion.activities.viewplayersheet.ViewPlayerSheetActivity
import jds.projects.ddcompanion.my_classes.adapters.PlayerSheetsAdapter.OnCardClickListener
import jds.projects.ddcompanion.my_classes.dd_classes.playersheet.PlayerSheet
import jds.projects.ddcompanion.my_classes.dialogs.DialogPlayerSheetManager
import jds.projects.ddcompanion.my_classes.listeners.HidingScrollListener
import jds.projects.ddcompanion.my_classes.utils.JDUtils

class PlayerSheetsFragment : Fragment() {

    private lateinit var viewModel: PlayerSheetsViewModel

    private lateinit var fabMain: FloatingActionButton
    private lateinit var fabCreatePlayerCard: ExtendedFloatingActionButton
    private lateinit var fabImportPlayerCard: ExtendedFloatingActionButton
    private lateinit var fabBGLayout: View
    private lateinit var showMainFabAnimation: Animation
    private lateinit var hideMainFabAnimation: Animation
    private lateinit var showSecondaryFabsAnimation: Animation
    private lateinit var hideSecondaryFabsAnimation: Animation

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_player_sheets, container, false)
        //init dei fabs

        fabMain = root.findViewById(R.id.fab_fragment_player_sheets_main_fab)
        fabBGLayout = root.findViewById(R.id.linearLayout_fragment_player_sheets_bg)
        fabCreatePlayerCard = root.findViewById(R.id.fab_fragment_player_sheets_create_player_card)
        fabImportPlayerCard = root.findViewById(R.id.fab_fragment_player_sheets_import_player_card)
        //init delle animations
        showMainFabAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.show_main_fab)
        hideMainFabAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.hide_main_fab)
        showSecondaryFabsAnimation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.show_secondary_fabs)
        hideSecondaryFabsAnimation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.hide_secondary_fabs)

        //ACTIONS DELLE ANIMAZIONI DEI FABS
        fabCreatePlayerCard.addOnHideAnimationListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {}
            override fun onAnimationCancel(p0: Animator?) {}
            override fun onAnimationStart(p0: Animator?) {}
            override fun onAnimationEnd(p0: Animator?) {
                fabCreatePlayerCard.isClickable = false
                fabCreatePlayerCard.isFocusable = false
                fabCreatePlayerCard.isEnabled = false
            }
        })

        fabCreatePlayerCard.addOnShowAnimationListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {}
            override fun onAnimationCancel(p0: Animator?) {}
            override fun onAnimationStart(p0: Animator?) {}
            override fun onAnimationEnd(p0: Animator?) {
                fabCreatePlayerCard.isClickable = true
                fabCreatePlayerCard.isFocusable = true
                fabCreatePlayerCard.isEnabled = true
            }
        })

        fabImportPlayerCard.addOnHideAnimationListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {}
            override fun onAnimationCancel(p0: Animator?) {}
            override fun onAnimationStart(p0: Animator?) {}
            override fun onAnimationEnd(p0: Animator?) {
                fabImportPlayerCard.isClickable = false
                fabImportPlayerCard.isFocusable = false
                fabImportPlayerCard.isEnabled = false
            }
        })
        fabImportPlayerCard.addOnShowAnimationListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {}
            override fun onAnimationCancel(p0: Animator?) {}
            override fun onAnimationStart(p0: Animator?) {}
            override fun onAnimationEnd(p0: Animator?) {
                fabImportPlayerCard.isClickable = true
                fabImportPlayerCard.isFocusable = true
                fabImportPlayerCard.isEnabled = true
            }
        })

        //INIT DEL RECYCLER VIEW
        val recyclerView =
            root.findViewById<RecyclerView>(R.id.recyclerview_fragment_player_sheets_player_sheets)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        //ottimizzazioni per la performance del recycler view
        recyclerView.setRecycledViewPool(RecyclerView.RecycledViewPool())
        recyclerView.setItemViewCacheSize(20)
        recyclerView.setHasFixedSize(true)
        //fine ottimizzazioni

        //init view model e del resto degli item grafici
        initFragment()
        //init adapter del recycler view
        val adapter = viewModel.createAdapter()

        adapter.setOnCardClickListener(object : OnCardClickListener {
            override fun onCardClicked(position: Int) {
                showPlayerSheet(viewModel.getPlayerSheets()[position])
            }

            override fun onCardLongClicked(position: Int): Boolean {
                val dialog =
                    DialogPlayerSheetManager(
                        requireContext(),
                        viewModel.getPlayerSheets()[position]
                    )

                dialog.setOnPlayerSheetActions(object :
                    DialogPlayerSheetManager.OnPlayerSheetListener {
                    override fun onPlayerSheetActionShow(playerSheet: PlayerSheet) {
                        showPlayerSheet(playerSheet)
                    }

                    override fun onPlayerSheetActionLevelUp(playerSheet: PlayerSheet) {
                        levelUpSheet(playerSheet)
                    }

                    override fun onPlayerSheetActionExport(playerSheet: PlayerSheet) {
                        if (viewModel.exportPlayerSheet(playerSheet))
                            Toast.makeText(
                                context,
                                resources.getString(R.string.player_sheet_exported_success),
                                Toast.LENGTH_SHORT
                            ).show()
                        else
                            Toast.makeText(
                                context,
                                resources.getString(R.string.player_sheet_exported_error),
                                Toast.LENGTH_SHORT
                            ).show()
                    }

                    override fun onPlayerSheetActionDelete(playerSheet: PlayerSheet) {
                        if (viewModel.deletePlayerSheet(playerSheet))
                            Toast.makeText(
                                context,
                                resources.getString(R.string.player_sheet_deleted_success),
                                Toast.LENGTH_SHORT
                            ).show()
                        else
                            Toast.makeText(
                                context,
                                resources.getString(R.string.player_sheet_deleted_error),
                                Toast.LENGTH_SHORT
                            ).show()
                    }
                })
                dialog.show()
                return true
            }
        })
        //collego adapter al recycler
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : HidingScrollListener() {
            override fun onHide() {
                fabMain.animate()
                    .translationY((fabMain.height + (fabMain.layoutParams as CoordinatorLayout.LayoutParams).bottomMargin).toFloat())
                    .setInterpolator(
                        AccelerateInterpolator(2F)
                    ).start()
            }

            override fun onShow() {
                fabMain.animate().translationY(0F).setInterpolator(DecelerateInterpolator(2F))
                    .start()
            }
        })
        return root
    }

    private fun showPlayerSheet(playerSheet: PlayerSheet) {
        val intent = Intent(requireContext(), ViewPlayerSheetActivity::class.java)
        intent.putExtra(JDUtils.BUNDLES.PLAYER_SHEET_ID, playerSheet.playerSheetID)
        startActivity(intent)
    }

    private fun levelUpSheet(playerSheet: PlayerSheet) {
        val intent = Intent(requireContext(), LevelUpPlayerSheetActivity::class.java)
        intent.putExtra(JDUtils.BUNDLES.PLAYER_SHEET_ID, playerSheet.playerSheetID)
        startActivityForResult(intent, JDUtils.REQUESTS.UPDATE_PLAYER_SHEET)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null)
            when (requestCode) {
                JDUtils.REQUESTS.CREATE_PLAYER_SHEET -> {
                    val newPlayerSheetID = data.getIntExtra(JDUtils.BUNDLES.PLAYER_SHEET_ID, 0)
                    if (newPlayerSheetID > 0)
                        viewModel.loadPlayerSheetWithID(newPlayerSheetID)
                }

                JDUtils.REQUESTS.UPDATE_PLAYER_SHEET -> {
                    val updatedPlayerSheetID = data.getIntExtra(JDUtils.BUNDLES.PLAYER_SHEET_ID, 0)
                    if (updatedPlayerSheetID > 0)
                        viewModel.updatePlayerSheetWithID(updatedPlayerSheetID)
                }
            }
    }

    private fun initFragment() {
        viewModel = PlayerSheetsViewModel(requireContext())
        viewModel.loadPlayerSheets()
        initFabsFunctionalities()
    }

    private fun initFabsFunctionalities() {
        //se premo sul main fab mostra e chiude il menu
        fabMain.setOnClickListener {
            if (viewModel.isFabOpen()) closeFABMenu() else showFABMenu()
        }
        //gestione creazione scheda personaggio
        fabCreatePlayerCard.setOnClickListener {
            closeFABMenu()
            val intent = Intent(context, CreatePlayerSheetActivity::class.java)
            startActivityForResult(intent, JDUtils.REQUESTS.CREATE_PLAYER_SHEET)
        }

        //gestione import scheda personaggio
        fabImportPlayerCard.setOnClickListener {
            closeFABMenu()
            val intent = Intent(context, ImportPlayerSheetActivity::class.java)
            startActivityForResult(intent, JDUtils.REQUESTS.CREATE_PLAYER_SHEET)
        }
        //se clicco su questa view(che prende tutto lo schermo), faccio chiudere il fab menu
        fabBGLayout.setOnClickListener { closeFABMenu() }
    }

    //Mostra il menu dei fabs secondari
    private fun showFABMenu() {
        if (!viewModel.isFabOpen()) {
            viewModel.setFabState(true)
            showEFAB(fabCreatePlayerCard)
            showEFAB(fabImportPlayerCard)
            fabBGLayout.visibility = View.VISIBLE
            fabMain.startAnimation(showMainFabAnimation)
        }
    }

    //Nasconde il menu dei fabs secondari
    private fun closeFABMenu() {
        if (viewModel.isFabOpen()) {
            viewModel.setFabState(false)
            hideEFAB(fabImportPlayerCard)
            hideEFAB(fabCreatePlayerCard)
            fabBGLayout.visibility = View.GONE
            fabMain.startAnimation(hideMainFabAnimation)
        }
    }

    private fun showEFAB(fab: ExtendedFloatingActionButton) {
        fab.startAnimation(showSecondaryFabsAnimation)
        fab.show()
    }

    private fun hideEFAB(fab: ExtendedFloatingActionButton) {
        fab.startAnimation(hideSecondaryFabsAnimation)
        fab.hide()
    }

    /***
     * Se il menu dei fabs button è aperto lo chiudo e non chiudo l'app
     */
    fun onBackPressed(): Boolean {
        return if (viewModel.isFabOpen()) {
            closeFABMenu()
            false
        } else
            true
    }

    fun notifyLanguageChanged() {
        viewModel.languageChanged()
    }

    fun notifyPlayerSheetLevelUpActivated(playerSheetID: Int) {
        viewModel.updatePlayerSheetWithID(playerSheetID)
    }
}