package com.masai.scanner.bottom_navigation

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.masai.scanner.R
import kotlinx.android.synthetic.main.navigation_header.*

class ContainerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var fragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)

        loadFragment(Fragment())
        fragmentManager = supportFragmentManager

        val bottomNavigationView = findViewById<View>(R.id.bottomNavigationView) as BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener {
            var navigationFragment: Fragment? = null
            when(it.itemId){
                R.id.home_navigation->{
                    navigationFragment = HomeFragment()
                }
                R.id.pdfTools_navigation->{
                    navigationFragment = PdfToolsFragment()
                }
            }
            if (navigationFragment!=null){
                loadFragment(navigationFragment)
            }
            return@setOnNavigationItemSelectedListener true
        }


        val toolbar: Toolbar = findViewById(R.id.toolbar0)
        setSupportActionBar(toolbar)

        val nav: NavigationView = findViewById(R.id.navMenu)
        nav.setNavigationItemSelectedListener(this)

        drawerLayout = findViewById(R.id.drawer)

        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val text1 = nav.findViewById<TextView>(R.id.signIn)
        
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
            R.id.setting -> Toast.makeText(
                this,
                "setting frgment need to added",
                Toast.LENGTH_SHORT
            ).show()
            R.id.shareApp -> Toast.makeText(
                this,
                "share app frgment need to added",
                Toast.LENGTH_SHORT
            ).show()
            //all content should add here
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}