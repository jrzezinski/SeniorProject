package group.project.buberapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class HelpFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_help, container, false);
    }
}


public class HelpItem {
    private String mTitle;
    private String mText;

    public HelpItem(String title, String text) {
        mTitle = title;
        mText = text;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getText() {
        return mText;
    }

    private static int lastHelpItemId = 0;
    public static ArrayList<HelpItem> createHelpItemsList(int numHelpItems) {
        ArrayList<HelpItem> helpItems = new ArrayList<HelpItem>();
//  WIP
//        for (int i = 1; i <= numHelpItems; i++) {
//            helpItems.add(new HelpItem("Help Item (Person) " + ++lastHelpItemId, i <= numHelpItems / 2));
//        }

        return helpItems;
    }
}