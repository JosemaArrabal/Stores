package dev.arrabaljosema.stores

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import dev.arrabaljosema.stores.databinding.ActivityMainBinding
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity(), OnClickListener {

    private lateinit var mBinding: ActivityMainBinding

    private lateinit var mAdapter: StoreAdapter
    private lateinit var mGridLayout: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        //Código temporal perteneciente a lo que se realizó en el activity_main.xml
        mBinding.btnSave.setOnClickListener {
            val store = StoreEntity(name = mBinding.etName.text.toString().trim())

            //Hilo secundario
            Thread {
                //Se guardan primero los datos en la BD
                StoreApplication.database.storeDao().addStore(store)
            }.start()

            mAdapter.add(store)
        }

        setupRecyclerview()
    }

    private fun setupRecyclerview() {
        mAdapter = StoreAdapter(mutableListOf(), this)
        mGridLayout = GridLayoutManager(this, 2)
        getStores()

        mBinding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = mGridLayout
            adapter = mAdapter
        }
    }

    private fun getStores() {

        // Con esto se efectúa la consulta de forma asíncrona
        doAsync {

            val stores = StoreApplication.database.storeDao().getAllStores()
            uiThread {

                mAdapter.setStores(stores)
            }
        }
    }

    /*
    * OnClickListener
    * */
    override fun onClick(storeEntity: StoreEntity) {

    }

    override fun onFavoriteStore(storeEntity: StoreEntity) {

        storeEntity.isFavorite = !storeEntity.isFavorite
        doAsync {
            StoreApplication.database.storeDao().updateStore(storeEntity)
            uiThread {
                mAdapter.update(storeEntity)
            }
        }
    }

    override fun onDeleteStore(storeEntity: StoreEntity) {
        doAsync {
            StoreApplication.database.storeDao().updateStore(storeEntity)
            uiThread {
                mAdapter.delete(storeEntity)
            }
        }
    }
}