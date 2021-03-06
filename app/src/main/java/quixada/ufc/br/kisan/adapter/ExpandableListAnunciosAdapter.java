package quixada.ufc.br.kisan.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


import quixada.ufc.br.kisan.R;
import quixada.ufc.br.kisan.activity.VisualizarPerfilActivity;
import quixada.ufc.br.kisan.chat.ChatActivity;
import quixada.ufc.br.kisan.util.CaminhosWebService;
import quixada.ufc.br.kisan.model.Livro;


public class ExpandableListAnunciosAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "ExpandableListAnunciosAdapter";
    String url_imagem = "http://"+ CaminhosWebService.IP+"/KisanSERVER/file/";

    private Context context;
    private ArrayList<Livro> livros;
    private boolean flagImage;
    private OnCustomClickListener callBack;


    public ExpandableListAnunciosAdapter(Context context, ArrayList<Livro> livros, OnCustomClickListener onCustomClickListeners) {
        this.context = context;
        this.livros = livros;
        this.flagImage = true;
        this.callBack = onCustomClickListeners;

    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.livros.get(groupPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Livro child = (Livro) getChild(groupPosition, childPosition);

        if (convertView == null) {
            Log.i(TAG, "entrou");
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item_fragment_anuncios, null);
        }

        TextView autor = (TextView) convertView.findViewById(R.id.autor);
        TextView genero = (TextView) convertView.findViewById(R.id.genero);
        final TextView sinopse = (TextView) convertView.findViewById(R.id.sinopse);
        TextView proprietario = (TextView) convertView.findViewById(R.id.proprietario);
        TextView localizacao = (TextView) convertView.findViewById(R.id.localizacao);
        ImageButton enviarMensagem = (ImageButton) convertView.findViewById(R.id.iniciarComunicacao);

        enviarMensagem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

               Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("token", child.getUsuario().getTokenGCM());
                intent.putExtra("idFacebook", child.getUsuario().getId_facebook());
                intent.putExtra("nomeUsuario", child.getUsuario().getNome());
                context.startActivity(intent);

            }
        });

        autor.setText(child.getAutor());
        genero.setText(child.getGenero());
        sinopse.setText(child.getSinopse());
        proprietario.setText(child.getUsuario().getNome());
        localizacao.setText(child.getUsuario().getCidade());

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.livros.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.livros.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        final Livro livro = livros.get(groupPosition);
        String headerTitle = livro.getTitulo();


        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group_fragment_anuncios, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        ImageView imagem = (ImageView) convertView.findViewById(R.id.capa);

        lblListHeader.setText(headerTitle);
        Picasso.with(context)
                .load(url_imagem+livro.getFoto())
                .placeholder(R.drawable.images)
                .error(R.drawable.images)
                .into(imagem);


        final ImageView favorite = (ImageView) convertView.findViewById(R.id.favoriteView);

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagImage) {
                    favorite.setImageResource(R.drawable.ic_favorite_black_24dp);
                    callBack.OnCustomClick(v, livro.getId());
                    flagImage = false;
                } else {
                    favorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    flagImage = true;
                }
            }
        });

        return convertView;
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}