package com.csci571.aditya.stockapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.csci571.aditya.stockapp.localstorage.AppStorage;
import com.csci571.aditya.stockapp.localstorage.FavoriteStorageModel;
import com.csci571.aditya.stockapp.models.DetailScreenWrapperModel;
import com.csci571.aditya.stockapp.network.StockAppClient;
import com.csci571.aditya.stockapp.news.NewsAdapter;
import com.csci571.aditya.stockapp.utils.Constants;
import com.csci571.aditya.stockapp.utils.Parser;

public class DetailActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    private final String TAG = "com.csci571.aditya.stockapp.DetailActivity";
    private String ticker;
    private boolean isFavorite;
    private DetailScreenWrapperModel data;
    private TextView sharesOwnedTextView;
    private TextView marketValueTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        ticker = intent.getStringExtra(Constants.INTENT_TICKER_EXTRA);
        isFavorite = AppStorage.isFavorite(getApplicationContext(), ticker);

        sharesOwnedTextView = findViewById(R.id.sharesOwnedTextView);
        marketValueTextView = findViewById(R.id.marketValueTextView);

        // setup toolbar
        Toolbar myToolbar = findViewById(R.id.detail_toolbar);
        myToolbar.setTitle(R.string.app_name);
        myToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        myToolbar.setNavigationOnClickListener(v -> {
            NavUtils.navigateUpFromSameTask(DetailActivity.this);
        });

        // setup menu and its functionality in the toolbar
        myToolbar.inflateMenu(R.menu.detail_menu);
        MenuItem menuItem = myToolbar.getMenu().findItem(R.id.action_favorite);
        if (isFavorite) {
            menuItem.setIcon(R.drawable.ic_baseline_star_24);
        }
        myToolbar.setOnMenuItemClickListener(this);

        ProgressBar progressBar = findViewById(R.id.detailProgressBar);
        TextView loadingTextView = findViewById(R.id.detailLoadingText);
        NestedScrollView nestedScrollView = findViewById(R.id.nested_scroll_view);

        NewsAdapter newsAdapter = new NewsAdapter();
        RecyclerView newsRecyclerView = nestedScrollView.findViewById(R.id.news_recycler_view);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        newsRecyclerView.setAdapter(newsAdapter);
        newsRecyclerView.setNestedScrollingEnabled(false);
        newsRecyclerView.setHasFixedSize(false);

        Button tradeButton = findViewById(R.id.tradeButton);
        tradeButton.setOnClickListener(this::showAlertDialogButtonClicked);

        nestedScrollView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        loadingTextView.setVisibility(View.VISIBLE);

        data = new DetailScreenWrapperModel();
        StockAppClient.getInstance().fetchDetailScreenData(ticker,
                progressBar, loadingTextView, nestedScrollView, newsAdapter, data, getApplicationContext());
    }

    public void showAlertDialogButtonClicked(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View tradeDialogLayout = getLayoutInflater().inflate(R.layout.trade_dialog, null);
        builder.setView(tradeDialogLayout);
        AlertDialog dialog = builder.create();

        TextView dialogHeaderTextView = tradeDialogLayout.findViewById(R.id.dialog_header);
        String dialogHeaderString = "Trade " + data.getOutlookModel().getCompanyName() + " shares";
        dialogHeaderTextView.setText(dialogHeaderString);

        TextView sharesComputeTextview = tradeDialogLayout.findViewById(R.id.shares_compute_textview);
        String sharesComputeString = "0 x $" + Parser.beautify(data.getSummaryModel().getLastPrice()) + "/share = $0.0";
        sharesComputeTextview.setText(sharesComputeString);

        TextView dialogShareResultTextview = tradeDialogLayout.findViewById(R.id.dialog_share_result_textview);
        double uninvestedCash = AppStorage.getUninvestedCash(getApplicationContext());
        String dialogShareResultString = "$" + Parser.beautify(uninvestedCash) + " available to buy " + ticker;
        dialogShareResultTextview.setText(dialogShareResultString);

        EditText shareValEditText = tradeDialogLayout.findViewById(R.id.share_val_textfield);
        shareValEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                double lastPrice = data.getSummaryModel().getLastPrice();
                double shares = 0;
                double total = 0;
                if (cs != null) {
                    String text = cs.toString();
                    if (text.length() > 0) {
                        try {
                            shares = Double.parseDouble(text);
                            total = shares * lastPrice;
                        } catch (NumberFormatException e) {
                            shares = 0;
                        }
                    }
                }
                String sharesComputeString = Parser.beautify(shares, 4) +
                        " x $" + Parser.beautify(lastPrice) + "/share = $" + Parser.beautify(total, 4);
                sharesComputeTextview.setText(sharesComputeString);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

            @Override
            public void afterTextChanged(Editable arg0) { }

        });

        double sharesOwned = AppStorage.getSharesOwned(getApplicationContext(), ticker);

        Button buyButton = tradeDialogLayout.findViewById(R.id.buybutton);
        buyButton.setOnClickListener(v -> {
            Editable editable = shareValEditText.getText();
            if (editable != null) {
                String sharesEntered = editable.toString();
                try {
                    double buyShares = Double.parseDouble(sharesEntered);
                    double newStockPrice = data.getSummaryModel().getLastPrice();
                    double moneyRequired = buyShares * newStockPrice;
                    double moneyAvailable = AppStorage.getUninvestedCash(getApplicationContext());
                    if (buyShares <= 0) {
                        Toast.makeText(getApplicationContext(), "Cannot buy less than 0 shares",
                                Toast.LENGTH_SHORT).show();
                    }
                    else if (moneyRequired > moneyAvailable) {
                        Toast.makeText(getApplicationContext(), "Not enough money to buy",
                                Toast.LENGTH_SHORT).show();
                    }
                    else {
                        double newShares = sharesOwned + buyShares;
                        double newMarketValue = newShares * newStockPrice;

                        AppStorage.updatePortfolioStock(getApplicationContext(), ticker, buyShares,
                                newStockPrice, true);

                        String sharesOwnedText = "Shares Owned: " + Parser.beautify(newShares, 4);
                        sharesOwnedTextView.setText(sharesOwnedText);
                        String marketValueText = "Market Value: " + Parser.beautify(newMarketValue, 4);
                        marketValueTextView.setText(marketValueText);
                        dialog.dismiss();
                        showSuccessDialog("You have successfully bought " + buyShares + " shares of " + ticker);
                    }
                } catch(NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Please enter valid amount",
                            Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(getApplicationContext(), "Please enter valid amount",
                        Toast.LENGTH_SHORT).show();
            }
        });

        Button sellButton = tradeDialogLayout.findViewById(R.id.sellbutton);
        sellButton.setOnClickListener(v -> {
            Editable editable = shareValEditText.getText();
            if (editable != null) {
                String sharesEntered = editable.toString();
                try {
                    double sellShares = Double.parseDouble(sharesEntered);
                    if (sellShares <= 0) {
                        Toast.makeText(getApplicationContext(), "Cannot sell less than 0 shares",
                                Toast.LENGTH_SHORT).show();
                    }
                    else if (sellShares > sharesOwned) {
                        Toast.makeText(getApplicationContext(), "Not enough shares to sell",
                                Toast.LENGTH_SHORT).show();
                    }
                    else {
                        double newShares = sharesOwned - sellShares;
                        double newStockPrice = data.getSummaryModel().getLastPrice();
                        double newMarketValue = newShares * newStockPrice;

                        AppStorage.updatePortfolioStock(getApplicationContext(), ticker, sellShares,
                                newStockPrice, false);

                        String sharesOwnedText;
                        String marketValueText;
                        if (newShares == 0) {
                            sharesOwnedText = "You have 0 shares of " + ticker + ".";
                            marketValueText = "Start trading!";
                        }
                        else {
                            sharesOwnedText = "Shares Owned: " + Parser.beautify(newShares, 4);
                            marketValueText = "Market Value: " + Parser.beautify(newMarketValue, 4);
                        }
                        sharesOwnedTextView.setText(sharesOwnedText);
                        marketValueTextView.setText(marketValueText);
                        dialog.dismiss();
                        showSuccessDialog("You have successfully sold " + sellShares + " shares of " + ticker);
                    }
                } catch(NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Please enter valid amount",
                            Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(getApplicationContext(), "Please enter valid amount",
                        Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void showSuccessDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View successDialogLayout = getLayoutInflater().inflate(R.layout.success_dialog, null);
        builder.setView(successDialogLayout);
        AlertDialog dialog = builder.create();
        TextView successMessageTextView = successDialogLayout.findViewById(R.id.success_message);
        successMessageTextView.setText(message);
        Button doneButton = successDialogLayout.findViewById(R.id.done_button);
        doneButton.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_favorite) {
            if (isFavorite) {
                item.setIcon(R.drawable.ic_baseline_star_border_24);
                AppStorage.removeFromFavorite(getApplicationContext(), ticker);
                Toast.makeText(this, "\"" + ticker + "\" was removed from favorites",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                item.setIcon(R.drawable.ic_baseline_star_24);
                String companyName = data.getOutlookModel().getCompanyName();
                double lastPrice = data.getSummaryModel().getLastPrice();
                FavoriteStorageModel favoriteStorageModel = new FavoriteStorageModel(ticker, companyName,
                        lastPrice, lastPrice);
                AppStorage.addToFavorite(getApplicationContext(), favoriteStorageModel);
                Toast.makeText(this, "\"" + ticker + "\" was added to favorites",
                        Toast.LENGTH_SHORT).show();
            }
            isFavorite = !isFavorite;
            return true;
        }
        return false;
    }
}