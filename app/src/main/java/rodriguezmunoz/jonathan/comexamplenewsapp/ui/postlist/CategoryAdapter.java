package rodriguezmunoz.jonathan.comexamplenewsapp.ui.postlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import rodriguezmunoz.jonathan.comexamplenewsapp.R;
import rodriguezmunoz.jonathan.comexamplenewsapp.data.model.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.VH> {
    private List<Category> categories = new ArrayList<>();
    private int selectedId = -1;
    private final Consumer<Integer> onCategorySelected;

    public CategoryAdapter(Consumer<Integer> onCategorySelected) {
        this.onCategorySelected = onCategorySelected;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_chip, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Category cat = categories.get(position);
        holder.chip.setText(cat.getName());
        holder.chip.setChecked(cat.getId() == selectedId);
        holder.chip.setOnClickListener(v -> {
            selectedId = cat.getId();
            onCategorySelected.accept(selectedId);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() { return categories.size(); }

    public void setCategories(List<Category> cats) {
        this.categories = cats;
        notifyDataSetChanged();
    }

    public void clearSelection() {
        selectedId = -1;
        notifyDataSetChanged();
    }

    static class VH extends RecyclerView.ViewHolder {
        Chip chip;
        VH(View v) {
            super(v);
            chip = v.findViewById(R.id.chip);
        }
    }
}