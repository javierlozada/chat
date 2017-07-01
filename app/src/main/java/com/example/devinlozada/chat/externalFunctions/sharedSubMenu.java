package com.example.devinlozada.chat.externalFunctions;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;

import com.example.devinlozada.chat.Login;
import com.example.devinlozada.chat.Perfil;
import com.example.devinlozada.chat.R;
import com.example.devinlozada.chat.biblia.biblia;
import com.google.firebase.auth.FirebaseAuth;


public class sharedSubMenu implements NavigationView.OnNavigationItemSelectedListener  {
    Context context;
    FirebaseAuth auth;
    DrawerLayout subMenu;

    /*receiving context to be set in onNavigationItemSelected function, to see whitch activity
   * should go*/
    public sharedSubMenu(final Context _context,DrawerLayout _submenu) {
        context = _context;
        subMenu = _submenu;

    }//end SharedMenu

    /*setting onNavigationItemSelected with items depending on menu created*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        auth = FirebaseAuth.getInstance();

        switch (item.getItemId()){
            case R.id.nav_camera:
                break;
            case R.id.nav_gallery:
                break;
            case R.id.nav_slideshow:
                break;
            case R.id.action_edit_profile:
                Intent perfil = new Intent(context, Perfil.class);
                context.startActivity(perfil);
                break;
            case R.id.biblia:
                Intent bible = new Intent(context,biblia.class);
                context.startActivity(bible);
                break;
            case R.id.cerrarSession:
                //when user clicks the logout button in the submenu a Alertdialog(small window) appears
                AlertDialog.Builder dialogo = new AlertDialog.Builder(context);
                dialogo.setMessage("¿Estas seguro que quieres cerrar session?");
                dialogo.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        auth.signOut();

                        Intent logout = new Intent(context,Login.class);
                        context.startActivity(logout);

                    }
                });

                dialogo.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //doesnt do anything
                    }
                });
                dialogo.show();
                break;
        }

        subMenu.closeDrawers();

        return true;
    }

}
