package com.thenikaran.guide;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thenikaran.guide.Fragments.ContactUs;
import com.thenikaran.guide.Fragments.Favorite;
import com.thenikaran.guide.Fragments.Home;
import com.thenikaran.guide.Fragments.About;


public class DrawerViewHolder extends RecyclerView.ViewHolder {
    TextView title;
    ImageView icon;

    public DrawerViewHolder(final Context context, View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        icon = (ImageView) itemView.findViewById(R.id.titleIcon);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDrawerAdapter.selected_item = getPosition();
                switch (getPosition()) {
                    case 0:
                        ((MainActivity) context).loadFragment(new Home(), false);
                        break;
                    case 1:
                        MainActivity.drawerLayout.closeDrawers();
                        Toast.makeText(context, "Coming Soon!", Toast.LENGTH_SHORT).show();
                        //context.startActivity(new Intent(context,Affiliate_main.class));
                        break;
                    case 2:
                        Fragment currentFragment = ((MainActivity) context).getSupportFragmentManager().findFragmentById(R.id.frameLayout);
                        FragmentTransaction transaction = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        transaction.add(R.id.frameLayout, new Favorite(), "fav");
                        transaction.hide(currentFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        break;
                    case 3:
                        MainActivity.drawerLayout.closeDrawers();
                        Fragment currentFragment1 = ((MainActivity) context).getSupportFragmentManager().findFragmentById(R.id.frameLayout);
                        FragmentTransaction transaction1 = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
                        transaction1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        transaction1.add(R.id.frameLayout, new ContactUs(), "cont");
                        transaction1.hide(currentFragment1);
                        transaction1.addToBackStack(null);
                        transaction1.commit();
                        break;
                    case 4:
                        // perform click on Rate Item
                        try {
                            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName())));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName())));
                        }
                        break;

                    case 5:
                        MainActivity.drawerLayout.closeDrawers();
                        Fragment currentFragment2 = ((MainActivity) context).getSupportFragmentManager().findFragmentById(R.id.frameLayout);
                        FragmentTransaction transaction2 = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
                        transaction2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        transaction2.add(R.id.frameLayout, new About(), "fav");
                        transaction2.hide(currentFragment2);
                        transaction2.addToBackStack(null);
                        transaction2.commit();

                        break;
                    case 6:
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=vignesh+s")));
                        break;
                }
                if (getAdapterPosition()!=3&&getAdapterPosition()!=5) {
                    MainActivity.title.setText("THENI GUIDE");
                    MainActivity.customDrawerAdapter.notifyDataSetChanged();
                    MainActivity.drawerLayout.closeDrawers();
                }

            }
        });
    }

}
