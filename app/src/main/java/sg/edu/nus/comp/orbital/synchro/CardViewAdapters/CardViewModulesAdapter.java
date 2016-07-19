package sg.edu.nus.comp.orbital.synchro.CardViewAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import sg.edu.nus.comp.orbital.synchro.DataHolders.ModuleList;
import sg.edu.nus.comp.orbital.synchro.R;

/**
 * Created by angja_000 on 10/6/2016.
 *
 * adapter for card views of modules
 * data set in the form of ModuleList objects
 */
public class CardViewModulesAdapter extends RecyclerView.Adapter<CardViewModulesAdapter.CardViewHolder> {

    private ArrayList<ModuleList> list;

    public CardViewModulesAdapter(ArrayList<ModuleList> list) {
        this.list  = list;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_modules_layout, parent, false);
        CardViewHolder cardViewHolder = new CardViewHolder(view);
        return cardViewHolder;
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        holder.moduleYearView.setText(list.get(position).getYear());
        holder.moduleSem1View.setText("Sem 1");
        holder.moduleListSem1.setText(list.get(position).getListSem1());

        holder.moduleSem2View.setText("Sem 2");
        holder.moduleListSem2.setText(list.get(position).getListSem2());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected class CardViewHolder extends RecyclerView.ViewHolder {

        private TextView moduleYearView, moduleListSem1, moduleListSem2, moduleSem1View, moduleSem2View;

        public CardViewHolder(View itemView) {
            super(itemView);
            moduleYearView = (TextView) itemView.findViewById(R.id.valueModuleYear);
            moduleSem1View = (TextView) itemView.findViewById(R.id.valueModuleSem1);
            moduleListSem1 = (TextView) itemView.findViewById(R.id.valueModulesTakenSem1);

            moduleSem2View = (TextView) itemView.findViewById(R.id.valueModuleSem2);
            moduleListSem2 = (TextView) itemView.findViewById(R.id.valueModulesTakenSem2);
        }

    }
}
