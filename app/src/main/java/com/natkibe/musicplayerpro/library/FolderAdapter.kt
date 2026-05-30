package com.natkibe.musicplayerpro.library
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.natkibe.musicplayerpro.R
class FolderAdapter(private val onClick: (AudioFolder)->Unit): RecyclerView.Adapter<FolderAdapter.VH>() { private val rows=mutableListOf<AudioFolder>(); fun submit(newRows: List<AudioFolder>){rows.clear(); rows.addAll(newRows); notifyDataSetChanged()}; override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=VH(LayoutInflater.from(parent.context).inflate(R.layout.row_folder,parent,false) as ViewGroup); override fun getItemCount()=rows.size; override fun onBindViewHolder(holder: VH, position: Int){val row=rows[position]; holder.title.text=row.name; holder.sub.text="${row.count} songs • ${row.storageType}"; holder.itemView.setOnClickListener{onClick(row)}}; class VH(root: ViewGroup): RecyclerView.ViewHolder(root){val title:TextView=root.findViewById(R.id.rowTitle); val sub:TextView=root.findViewById(R.id.rowSubtitle)} }
