package com.tirdis.watchorread.view;

/**
 * Created by sb on 6/26/15.
 */


import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;
import com.tirdis.watchorread.adapter.CaldroidSampleCustomAdapter;

public class CaldroidSampleCustomFragment extends CaldroidFragment {

    @Override
    public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
        // TODO Auto-generated method stub
        return new CaldroidSampleCustomAdapter(getActivity(), month, year,
                getCaldroidData(), extraData);
    }

}



