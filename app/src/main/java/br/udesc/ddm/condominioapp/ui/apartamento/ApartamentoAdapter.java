package br.udesc.ddm.condominioapp.ui.apartamento;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.udesc.ddm.condominioapp.R;
import br.udesc.ddm.condominioapp.data.model.Apartamento;
import br.udesc.ddm.condominioapp.utils.NumberFormatter;

import java.util.ArrayList;
import java.util.List;

public class ApartamentoAdapter extends RecyclerView.Adapter<ApartamentoAdapter.ApartamentoViewHolder> {

    private final Context context;
    private final List<Apartamento> apartamentos;
    private final ApartamentoListener listener;

    public interface ApartamentoListener {
        void onLocatarioInfoClick(Apartamento apartamento);
        void onEditClick(Apartamento apartamento);
        void onDeleteClick(Apartamento apartamento);
        void onAssociarLocatarioClick(Apartamento apartamento);
        void onDesassociarLocatarioClick(Apartamento apartamento);
    }

    public ApartamentoAdapter(Context context, ApartamentoListener listener) {
        this.context = context;
        this.apartamentos = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ApartamentoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_apartamento, parent, false);
        return new ApartamentoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApartamentoViewHolder holder, int position) {
        Apartamento apartamento = apartamentos.get(position);
        holder.bind(apartamento);
    }

    @Override
    public int getItemCount() {
        return apartamentos.size();
    }

    public void updateList(List<Apartamento> apartamentos) {
        this.apartamentos.clear();
        if (apartamentos != null) {
            this.apartamentos.addAll(apartamentos);
        }
        notifyDataSetChanged();
    }

    class ApartamentoViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvNumero;
        private final TextView tvLocatario;
        private final TextView tvDetalhes;
        private final Button btnInfoLocatario;
        private final Button btnEditar;
        private final ImageView ivMenu;

        public ApartamentoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumero = itemView.findViewById(R.id.tvNumero);
            tvLocatario = itemView.findViewById(R.id.tvLocatario);
            tvDetalhes = itemView.findViewById(R.id.tvDetalhes);
            btnInfoLocatario = itemView.findViewById(R.id.btnInfoLocatario);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            ivMenu = itemView.findViewById(R.id.ivMenu);
        }

        public void bind(final Apartamento apartamento) {
            tvNumero.setText("Apartamento " + apartamento.getNumero());

            if (apartamento.getLocatario() != null && !apartamento.getLocatario().getNome().isEmpty()) {
                tvLocatario.setText("Locatário: " + apartamento.getLocatario().getNome());
                btnInfoLocatario.setText("Info Locatário");
                btnInfoLocatario.setEnabled(true);
            } else {
                tvLocatario.setText("Locatário: Não definido");
                btnInfoLocatario.setText("Associar");
                btnInfoLocatario.setEnabled(true);
            }

            String metragem = String.format("%.1f m²", apartamento.getMetragem());
            String vagas = String.format("%d", apartamento.getVagasDeGaragem());
            String valorAluguel = NumberFormatter.formatCurrency(apartamento.getValorAluguel());

            String detalhes = String.format("Metragem: %s | Vagas: %s | Aluguel: %s",
                    metragem, vagas, valorAluguel);
            tvDetalhes.setText(detalhes);

            btnInfoLocatario.setOnClickListener(v -> {
                if (listener != null) {
                    if (apartamento.getLocatario() != null && !apartamento.getLocatario().getNome().isEmpty()) {
                        listener.onLocatarioInfoClick(apartamento);
                    } else {
                        listener.onAssociarLocatarioClick(apartamento);
                    }
                }
            });

            btnEditar.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditClick(apartamento);
                }
            });

            ivMenu.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(context, ivMenu);
                popup.inflate(R.menu.menu_apartamento_item);

                MenuItem menuAssociar = popup.getMenu().findItem(R.id.action_associate);
                MenuItem menuDesassociar = popup.getMenu().findItem(R.id.action_disassociate);

                if (apartamento.getLocatario() != null && !apartamento.getLocatario().getNome().isEmpty()) {
                    menuAssociar.setVisible(false);
                    menuDesassociar.setVisible(true);
                } else {
                    menuAssociar.setVisible(true);
                    menuDesassociar.setVisible(false);
                }

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.action_edit) {
                            if (listener != null) {
                                listener.onEditClick(apartamento);
                            }
                            return true;
                        } else if (id == R.id.action_delete) {
                            if (listener != null) {
                                listener.onDeleteClick(apartamento);
                            }
                            return true;
                        } else if (id == R.id.action_associate) {
                            if (listener != null) {
                                listener.onAssociarLocatarioClick(apartamento);
                            }
                            return true;
                        } else if (id == R.id.action_disassociate) {
                            if (listener != null) {
                                listener.onDesassociarLocatarioClick(apartamento);
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