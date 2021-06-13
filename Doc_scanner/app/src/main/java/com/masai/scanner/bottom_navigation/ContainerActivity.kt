package com.masai.scanner.bottom_navigation

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.masai.scanner.R
import com.masai.scanner.side_drawer.SettingFragment
import kotlinx.android.synthetic.main.navigation_header.*


class ContainerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var fragmentManager: FragmentManager
    private lateinit var nav: NavigationView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var headerView: View
    var navigationFragment: Fragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)

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
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        val menuItem = menu?.findItem(R.id.searchBar)
        val searchView = menuItem?.actionView as SearchView
        searchView.queryHint
        return super.onCreateOptionsMenu(menu)
    }


}