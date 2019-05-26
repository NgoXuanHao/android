package com.example.cuoiki.note;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.example.cuoiki.R;

public abstract class MainActionModeCallback implements ActionMode.Callback {
    private ActionMode action;
    private MenuItem countItem;
    private MenuItem shareItem;

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        actionMode.getMenuInflater().inflate(R.menu.main_action_mode, menu);
        this.countItem = menu.findItem(R.id.action_delete_notes);
        this.shareItem = menu.findItem(R.id.share_note);
        this.action = actionMode;
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {

    }

    public void setCount(String checkedCount) {
        if (countItem != null)
            this.countItem.setTitle(checkedCount);
    }


    public ActionMode getAction() {
        return action;
    }

    public void changeShareItemVisible(boolean b) {
        shareItem.setVisible(b);
    }
}
