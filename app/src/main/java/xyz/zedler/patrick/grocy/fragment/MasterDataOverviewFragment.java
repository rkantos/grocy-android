package xyz.zedler.patrick.grocy.fragment;

/*
    This file is part of Grocy Android.

    Grocy Android is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Grocy Android is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Grocy Android.  If not, see <http://www.gnu.org/licenses/>.

    Copyright 2020 by Patrick Zedler & Dominic Zedler
*/

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.FragmentNavigator;

import xyz.zedler.patrick.grocy.R;
import xyz.zedler.patrick.grocy.activity.MainActivity;
import xyz.zedler.patrick.grocy.api.GrocyApi;
import xyz.zedler.patrick.grocy.databinding.FragmentMasterDataOverviewBinding;
import xyz.zedler.patrick.grocy.helper.InfoFullscreenHelper;
import xyz.zedler.patrick.grocy.util.Constants;
import xyz.zedler.patrick.grocy.viewmodel.MasterDataOverviewViewModel;

public class MasterDataOverviewFragment extends BaseFragment {

    private final static String TAG = MasterDataOverviewFragment.class.getSimpleName();

    private MainActivity activity;
    private FragmentMasterDataOverviewBinding binding;
    private MasterDataOverviewViewModel viewModel;
    private InfoFullscreenHelper infoFullscreenHelper;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentMasterDataOverviewBinding.inflate(
                inflater, container, false
        );
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(infoFullscreenHelper != null) {
            infoFullscreenHelper.destroyInstance();
            infoFullscreenHelper = null;
        }
        if(binding != null) {
            binding = null;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        activity = (MainActivity) requireActivity();

        viewModel = new ViewModelProvider(this).get(MasterDataOverviewViewModel.class);
        viewModel.setOfflineLive(!activity.isOnline());

        binding.back.setOnClickListener(v -> activity.navigateUp());

        infoFullscreenHelper = new InfoFullscreenHelper(binding.coordinatorContainer);
        binding.linearProducts.setOnClickListener(v -> navigate(
                MasterDataOverviewFragmentDirections
                        .actionMasterDataOverviewFragmentToMasterObjectListFragment(
                                GrocyApi.ENTITY.PRODUCTS
                        ).setTransitionName(getString(R.string.transition_overview_products)),
                (new FragmentNavigator.Extras.Builder()).addSharedElement(
                        binding.titleProducts,
                        getString(R.string.transition_overview_products)
                ).build())
        );
        binding.linearQuantityUnits.setOnClickListener(v -> navigate(
                MasterDataOverviewFragmentDirections
                        .actionMasterDataOverviewFragmentToMasterObjectListFragment(
                                GrocyApi.ENTITY.QUANTITY_UNITS
                        ).setTransitionName(getString(R.string.transition_overview_qus)),
                (new FragmentNavigator.Extras.Builder()).addSharedElement(
                        binding.titleQuantityUnits,
                        getString(R.string.transition_overview_qus)
                ).build())
        );
        binding.linearLocations.setOnClickListener(v -> navigate(
                MasterDataOverviewFragmentDirections
                        .actionMasterDataOverviewFragmentToMasterObjectListFragment(
                                GrocyApi.ENTITY.LOCATIONS
                        ).setTransitionName(getString(R.string.transition_overview_locations)),
                (new FragmentNavigator.Extras.Builder()).addSharedElement(
                        binding.titleLocations,
                        getString(R.string.transition_overview_locations)
                ).build())
        );
        binding.linearProductGroups.setOnClickListener(v -> navigate(
                MasterDataOverviewFragmentDirections
                        .actionMasterDataOverviewFragmentToMasterObjectListFragment(
                                GrocyApi.ENTITY.PRODUCT_GROUPS
                        ).setTransitionName(getString(R.string.transition_overview_product_groups)),
                (new FragmentNavigator.Extras.Builder()).addSharedElement(
                        binding.titleProductGroups,
                        getString(R.string.transition_overview_product_groups)
                ).build())
        );
        binding.linearStores.setOnClickListener(v -> navigate(
                MasterDataOverviewFragmentDirections
                        .actionMasterDataOverviewFragmentToMasterObjectListFragment(
                                GrocyApi.ENTITY.STORES
                        ).setTransitionName(getString(R.string.transition_overview_stores)),
                (new FragmentNavigator.Extras.Builder()).addSharedElement(
                        binding.titleStores,
                        getString(R.string.transition_overview_stores)
                ).build())
        );

        viewModel.getIsLoadingLive().observe(getViewLifecycleOwner(), state -> {
            binding.swipe.setRefreshing(state);
            if(!state) viewModel.setCurrentQueueLoading(null);
        });

        viewModel.getInfoFullscreenLive().observe(
                getViewLifecycleOwner(),
                infoFullscreen -> infoFullscreenHelper.setInfo(infoFullscreen)
        );

        viewModel.getOfflineLive().observe(getViewLifecycleOwner(), this::appBarOfflineInfo);

        viewModel.getProductsLive().observe(
                getViewLifecycleOwner(),
                products -> binding.countProducts.setText(
                        products != null ? getString(R.string.subtitle_count, products.size())
                                : getString(R.string.subtitle_count_unknown)
                )
        );
        viewModel.getLocationsLive().observe(
                getViewLifecycleOwner(),
                locations -> binding.countLocations.setText(
                        locations != null ? getString(R.string.subtitle_count, locations.size())
                                : getString(R.string.subtitle_count_unknown)
                )
        );
        viewModel.getLocationsLive().observe(
                getViewLifecycleOwner(),
                locations -> binding.countStores.setText(
                        locations != null ? getString(R.string.subtitle_count, locations.size())
                                : getString(R.string.subtitle_count_unknown)
                )
        );
        viewModel.getStoresLive().observe(
                getViewLifecycleOwner(),
                stores -> binding.countQuantityUnits.setText(
                        stores != null ? getString(R.string.subtitle_count, stores.size())
                                : getString(R.string.subtitle_count_unknown)
                )
        );
        viewModel.getProductGroupsLive().observe(
                getViewLifecycleOwner(),
                productGroups -> binding.countProductGroups.setText(
                        productGroups != null
                                ? getString(R.string.subtitle_count, productGroups.size())
                                : getString(R.string.subtitle_count_unknown)
                )
        );

        binding.swipe.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        if(savedInstanceState == null) viewModel.loadFromDatabase(true);

        // UPDATE UI
        updateUI((getArguments() == null
                || getArguments().getBoolean(Constants.ARGUMENT.ANIMATED, true))
                && savedInstanceState == null);
    }

    private void updateUI(boolean animated) {
        activity.showHideDemoIndicator(this, animated);
        activity.getScrollBehavior().setUpScroll(binding.scroll);
        activity.getScrollBehavior().setHideOnScroll(true);
        activity.updateBottomAppBar(
                Constants.FAB.POSITION.GONE,
                R.menu.menu_empty,
                animated,
                () -> {}
        );
    }

    @Override
    public void updateConnectivity(boolean online) {
        if(!online == viewModel.isOffline()) return;
        viewModel.setOfflineLive(!online);
        viewModel.downloadData();
    }

    private void appBarOfflineInfo(boolean visible) {
        boolean currentState = binding.linearOfflineError.getVisibility() == View.VISIBLE;
        if(visible == currentState) return;
        binding.linearOfflineError.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @NonNull
    @Override
    public String toString() {
        return TAG;
    }
}
