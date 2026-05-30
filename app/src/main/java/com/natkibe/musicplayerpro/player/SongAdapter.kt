package com.natkibe.musicplayerpro.player
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.natkibe.musicplayerpro.R
import com.natkibe.musicplayerpro.data.AudioItemEntity
class SongAdapter(private val onClick:(AudioItemEntity)->Unit): RecyclerView.Adapter<SongAdapter.VH>() { private val rows=mutableListOf<AudioItemEntity>(); fun submit(newRows:List<AudioItemEntity>){rows.clear(); rows.addAll(newRows); notifyDataSetChanged()}; override fun onCreateViewHolder(parent:ViewGroup, viewType:Int)=VH(LayoutInflater.from(parent.context).inflate(R.layout.row_song,parent,false) as ViewGroup); override fun getItemCount()=rows.size; override fun onBindViewHolder(holder:VH, position:Int){val row=rows[position]; holder.title.text=row.title; holder.sub.text=listOfNotNull(row.artist,row.album).joinToString(" • ").ifBlank{row.mimeType?:"audio"}; holder.itemView.setOnClickListener{onClick(row)}}; class VH(root:ViewGroup): RecyclerView.ViewHolder(root){val title:TextView=root.findViewById(R.id.rowTitle); val sub:TextView=root.findViewById(R.id.rowSubtitle)} }
