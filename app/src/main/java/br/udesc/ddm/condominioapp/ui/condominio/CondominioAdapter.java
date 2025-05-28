package br.udesc.ddm.condominioapp.ui.condominio;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.udesc.ddm.condominioapp.R;
import br.udesc.ddm.condominioapp.data.model.Condominio;
import br.udesc.ddm.condominioapp.utils.NumberFormatter;

import java.util.ArrayList;
import java.util.List;

public class CondominioAdapter extends RecyclerView.Adapter<CondominioAdapter.CondominioViewHolder> {

    private final Context context;
    private final List<Condominio> condominios;
    private final CondominioListener listener;

    public interface CondominioListener {
        void onVerBlocosClick(Condominio condominio);
        void onEditClick(Condominio condominio);
        void onDeleteClick(Condominio condominio);
    }

    public CondominioAdapter(Context context, CondominioListener listener) {
        this.context = context;
        this.condominios = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public CondominioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_condominio, parent, false);
        return new CondominioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CondominioViewHolder holder, int position) {
        Condominio condominio = condominios.get(position);
        holder.bind(condominio);
    }

    @Override
    public int getItemCount() {
        return condominios.size();
    }

    public void updateList(List<Condominio> condominios) {
        this.condominios.clear();
        if (condominios != null) {
            this.condominios.addAll(condominios);
        }
        notifyDataSetChanged();
    }

    class CondominioViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvNome;
        private final TextView tvEndereco;
        private final TextView tvValores;
        private final Button btnVerBlocos;
        private final Button btnEditar;
        private final ImageButton btnVerLocalizacao;
        private final ImageView ivMenu;

        public CondominioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvNome);
            tvEndereco = itemView.findViewById(R.id.tvEndereco);
            tvValores = itemView.findViewById(R.id.tvValores);
            btnVerBlocos = itemView.findViewById(R.id.btnVerBlocos);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnVerLocalizacao = itemView.findViewById(R.id.btnVerLocalizacao);
            ivMenu = itemView.findViewById(R.id.ivMenu);
        }

        public void bind(final Condominio condominio) {
            tvNome.setText(condominio.getNome());

            String enderecoCompleto = condominio.getEnderecoCompleto();
            if (enderecoCompleto != null && !enderecoCompleto.trim().isEmpty()) {
                tvEndereco.setText(enderecoCompleto);
                tvEndereco.setVisibility(View.VISIBLE);
            } else {
                tvEndereco.setText("Endereço não informado");
                tvEndereco.setVisibility(View.VISIBLE);
            }

            String taxaMensal = NumberFormatter.formatCurrency(condominio.getTaxaMensalCondominio());
            String valorMetro = NumberFormatter.formatCurrency(condominio.getFatorMultiplicadorDeMetragem());
            String valorGaragem = NumberFormatter.formatCurrency(condominio.getValorVagaGaragem());

            tvValores.setText(String.format("Taxa: %s | Valor m²: %s | Garagem: %s",
                    taxaMensal, valorMetro, valorGaragem));

            btnVerBlocos.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onVerBlocosClick(condominio);
                }
            });

            btnEditar.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditClick(condominio);
                }
            });

            btnVerLocalizacao.setOnClickListener(v -> {
                Intent intent = new Intent(context, MapaCondominioActivity.class);
                intent.putExtra("endereco", condominio.getEnderecoCompleto());
                intent.putExtra("nome", condominio.getNome());
                context.startActivity(intent);
            });

            ivMenu.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(context, ivMenu);
                popup.inflate(R.menu.menu_condominio_item);
                popup.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.action_edit) {
                        if (listener != null) {
                            listener.onEditClick(condominio);
                        }
                        return true;
                    } else if (item.getItemId() == R.id.action_delete) {
                        if (listener != null) {
                            listener.onDeleteClick(condominio);
                        }
                        return true;
                    }
                    return false;
                });
                popup.show();
            });
        }
    }
}