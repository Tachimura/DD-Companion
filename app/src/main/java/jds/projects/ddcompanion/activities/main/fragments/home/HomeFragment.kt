package jds.projects.ddcompanion.activities.main.fragments.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.activities.gamemanualdetails.GameManualDetailsActivity
import jds.projects.ddcompanion.my_classes.adapters.GameManualsAdapter
import jds.projects.ddcompanion.my_classes.cards.GameManualCardView
import jds.projects.ddcompanion.my_classes.dd_classes.gamemanual.GameManual
import jds.projects.ddcompanion.my_classes.io.network.JDNetworkManager
import jds.projects.ddcompanion.my_classes.io.network.NetworkRequester
import jds.projects.ddcompanion.my_classes.utils.JDUtils
import org.json.JSONObject

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var latestCard: GameManualCardView
    private lateinit var coordinatorNotify: CoordinatorLayout
    private lateinit var btnCheckUpdates: Button
    private lateinit var recyclerView: RecyclerView
    private var listener: HomeFragmentListener? = null

    interface HomeFragmentListener {
        fun onNewGameManualDownloaded()
    }

    fun setOnHomeFragmentListener(listener: HomeFragmentListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = HomeViewModel(requireContext())
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        coordinatorNotify = root.findViewById(R.id.coordinator_layout_fragment_home_notify)
        //GESTIONE PARTE BASSA DEL LAYOUT
        //prendo il recyclerview
        recyclerView = root.findViewById(R.id.recyclerview_fragment_home_gamemanuals)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        //ottimizzazioni per la performance del recycler view
        recyclerView.setRecycledViewPool(RecyclerView.RecycledViewPool())
        recyclerView.setItemViewCacheSize(20)
        recyclerView.setHasFixedSize(true)
        //fine ottimizzazioni
        //init adapter del recycler view
        viewModel.loadGameManualsBaseInfo()
        viewModel.setAdapter(GameManualsAdapter(viewModel.getGameManualsBaseInfo(), false))

        viewModel.getAdapter().setOnCardClickListener(object :
            GameManualsAdapter.OnAdapterCardClickListener {
            override fun onCardClick(position: Int) {
                onManualClick(viewModel.getGameManualsBaseInfo()[position])
            }
        })
        //collego adapter al recycler
        recyclerView.adapter = viewModel.getAdapter()

        //gestione parte alta del layout
        //ultimo manuale di gioco
        latestCard =
            GameManualCardView(
                root.findViewById(R.id.expandablecv_fragment_home__latestmanual),
                requireContext()
            )

        latestCard.updateCard(viewModel.getLatestGameManual())
        latestCard.setOnCardClickListener(object : GameManualCardView.OnCardClickListener {
            override fun onCardClick() {
                onManualClick(viewModel.getLatestGameManual())
            }
        })

        //pulsante x update sul server
        btnCheckUpdates = root.findViewById(R.id.btnHomeTest)
        btnCheckUpdates.setOnClickListener {
            if (JDNetworkManager.isNetworkAvailable(requireContext())) run {
                //creo la richiesta e la eseguo
                val requester = NetworkRequester().apply {
                    setOnNetworkRequesterListener(object :
                        NetworkRequester.NetworkRequesterListener {
                        override fun onRequestStart(postParams: HashMap<String, String>) {}
                        override fun onRequestEnd(result: JSONObject) {
                            checkForMoreUpdatesIfNeeded(result)
                        }
                    })
                }
                requester.prepareRequest(NetworkRequester.REQUEST.CHECK_LATEST_GAME_MANUAL)
            }
        }
        return root
    }

    private fun onManualClick(manual: GameManual) {
        val intent = Intent(context, GameManualDetailsActivity::class.java)
        intent.putExtra(JDUtils.BUNDLES.GAME_MANUAL_ID, manual.manualID)
        startActivity(intent)
    }

    private fun checkForMoreUpdatesIfNeeded(result: JSONObject) {
        val resultRequest = result.getInt("success")
        if (resultRequest > 0) {
            //prendo lo 0 xkè so che ci sarà solo 1 record
            val manualInResult = result.getJSONArray("result").getJSONObject(0)
            val manualID = manualInResult.getInt("latest_manual_id")
            val manualVersion = manualInResult.getDouble("latest_manual_version")
            val manualData = manualInResult.getString("latest_manual_release_data")
            val manualReceived =
                GameManual(
                    manualID,
                    manualVersion.toFloat(),
                    manualData
                )
            activity?.runOnUiThread {
                //SE esiste una nuova versione chiedo all'utente se vuole aggiornare
                if (viewModel.isLatestGameManualOutdated(manualReceived)) {
                    val notify = Snackbar.make(
                        btnCheckUpdates,
                        resources.getString(R.string.fragment_home_snackbar_updatefound),
                        Snackbar.LENGTH_LONG
                    ).apply {
                        anchorView = coordinatorNotify
                        setAction(resources.getString(R.string.fragment_home_snackbar_updatefound_action)) { requestUpdate() }
                    }
                    notify.show()
                } else
                //altrimenti gli dico che non ci sono aggiornamenti nuovi
                    Snackbar.make(
                        btnCheckUpdates,
                        resources.getString(R.string.fragment_home_snackbar_latestupdate),
                        Snackbar.LENGTH_SHORT
                    ).setAnchorView(coordinatorNotify).show()
            }
        }
    }

    private fun requestUpdate() {
        if (JDNetworkManager.isNetworkAvailable(requireContext())) run {
            //creo la richiesta e la eseguo
            val bar: Snackbar =
                Snackbar.make(
                    btnCheckUpdates,
                    resources.getString(R.string.fragment_home_snackbar_downloading),
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAnchorView(coordinatorNotify)
            val contentLay = bar.view
                .findViewById<View>(R.id.snackbar_text)
                .parent as ViewGroup
            val item = ProgressBar(context)
            contentLay.addView(item)
            bar.show()

            val requester = NetworkRequester().apply {
                setOnNetworkRequesterListener(object : NetworkRequester.NetworkRequesterListener {
                    override fun onRequestStart(postParams: HashMap<String, String>) {
                        postParams["manual_id"] =
                            viewModel.getLatestGameManual().manualVersion.toString()
                    }

                    override fun onRequestEnd(result: JSONObject) {
                        if (result.getInt("success") > 0) {
                            val nNewManuals =
                                viewModel.updateGameManualsDatabase(
                                    result.getJSONArray("result"),
                                    result.getJSONArray("languages")
                                )
                            activity?.runOnUiThread {
                                Toast.makeText(
                                    requireContext(),
                                    resources.getString(
                                        R.string.fragment_home_snackbar_downloaded,
                                        nNewManuals
                                    ),
                                    Toast.LENGTH_SHORT
                                ).show()
                                //aggiorno la cardview con l'ultimo elemento
                                latestCard.updateCard(viewModel.updateLatestGameManual())
                                viewModel.updateAdapter()
                                //indico che ci sono nuovi manuali di gioco scaricati
                                listener?.onNewGameManualDownloaded()
                            }
                        }
                        bar.dismiss()
                    }
                })
            }
            requester.prepareRequest(NetworkRequester.REQUEST.DOWNLOAD_NEW_GAME_MANUALS)
        }
    }
}