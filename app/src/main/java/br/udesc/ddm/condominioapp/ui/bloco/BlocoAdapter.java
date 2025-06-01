package br.udesc.ddm.condominioapp.ui.bloco;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.udesc.ddm.condominioapp.R;
import br.udesc.ddm.condominioapp.data.model.Bloco;

import java.util.ArrayList;
import java.util.List;

public class BlocoAdapter extends RecyclerView.Adapter<BlocoAdapter.BlocoViewHolder> {

    private final Context context;
    private final List<Bloco> blocos;
    private final BlocoListener listener;

    public interface BlocoListener {
        void onVerApartamentosClick(Bloco bloco);
        void onEditClick(Bloco bloco);
        void onDeleteClick(Bloco bloco);
    }

    public BlocoAdapter(Context context, BlocoListener listener) {
        this.context = context;
        this.blocos = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public BlocoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bloco, parent, false);
        return new BlocoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlocoViewHolder holder, int position) {
        Bloco bloco = blocos.get(position);
        holder.bind(bloco);
    }

    @Override
    public int getItemCount() {
        return blocos.size();
    }

    public void updateList(List<Bloco> blocos) {
        this.blocos.clear();
        if (blocos != null) {
            this.blocos.addAll(blocos);
        }
        notifyDataSetChanged();
    }

    class BlocoViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvNome;
        private final TextView tvDetalhes;
        private final Button btnVerApartamentos;
        private final Button btnEditar;
        private final ImageButton btnDelete;


        public BlocoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvNome);
            tvDetalhes = itemView.findViewById(R.id.tvDetalhes);
            btnVerApartamentos = itemView.findViewById(R.id.btnVerApartamentos);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnDelete   = itemView.findViewById(R.id.btnDelete);
        }

        public void bind(final Bloco bloco) {
            tvNome.setText(bloco.getNome());

            String detalhes = String.format("Andares: %d | Vagas de Garagem: %d",
                    bloco.getAndares(), bloco.getVagasDeGaragem());
            tvDetalhes.setText(detalhes);

            btnVerApartamentos.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onVerApartamentosClick(bloco);
                }
            });

            btnEditar.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditClick(bloco);
                }
            });
            btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(bloco);
                }
            });
        }
    }
}