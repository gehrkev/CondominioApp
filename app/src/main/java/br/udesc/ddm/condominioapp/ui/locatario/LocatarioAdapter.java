package br.udesc.ddm.condominioapp.ui.locatario;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.udesc.ddm.condominioapp.R;
import br.udesc.ddm.condominioapp.data.model.Locatario;

import java.util.ArrayList;
import java.util.List;

public class LocatarioAdapter extends RecyclerView.Adapter<LocatarioAdapter.LocatarioViewHolder> {

    private final Context context;
    private final List<Locatario> locatarios;
    private final LocatarioListener listener;

    public interface LocatarioListener {
        void onItemClick(Locatario locatario);
        void onEditClick(Locatario locatario);
        void onDeleteClick(Locatario locatario);
    }

    public LocatarioAdapter(Context context, LocatarioListener listener) {
        this.context = context;
        this.locatarios = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public LocatarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_locatario, parent, false);
        return new LocatarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocatarioViewHolder holder, int position) {
        Locatario locatario = locatarios.get(position);
        holder.bind(locatario);
    }

    @Override
    public int getItemCount() {
        return locatarios.size();
    }

    public void updateList(List<Locatario> locatarios) {
        this.locatarios.clear();
        if (locatarios != null) {
            this.locatarios.addAll(locatarios);
        }
        notifyDataSetChanged();
    }

    class LocatarioViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvNome;
        private final TextView tvCpf;
        private final TextView tvContato;
        private final TextView tvApartamento;
        private final ImageView ivMenu;

        public LocatarioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvNome);
            tvCpf = itemView.findViewById(R.id.tvCpf);
            tvContato = itemView.findViewById(R.id.tvContato);
            tvApartamento = itemView.findViewById(R.id.tvApartamento);
            ivMenu = itemView.findViewById(R.id.ivMenu);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(locatarios.get(position));
                }
            });
        }

        public void bind(final Locatario locatario) {
            tvNome.setText(locatario.getNome());
            tvCpf.setText("CPF: " + locatario.getCpf());

            String contato = String.format("Tel: %s | Email: %s",
                    locatario.getTelefone(), locatario.getEmail());
            tvContato.setText(contato);

            if (locatario.temApartamento()) {
                tvApartamento.setText("Tem apartamento associado");
                tvApartamento.setVisibility(View.VISIBLE);
            } else {
                tvApartamento.setVisibility(View.GONE);
            }

            ivMenu.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(context, ivMenu);
                popup.inflate(R.menu.menu_locatario_item);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.action_edit) {
                            if (listener != null) {
                                listener.onEditClick(locatario);
                            }
                            return true;
                        } else if (item.getItemId() == R.id.action_delete) {
                            if (listener != null) {
                                listener.onDeleteClick(locatario);
                            }
                            return true;
                        }
                        return false;
                    }
                });
                popup.show();
            });
        }
    }
}