package umu.kaoq0003.thirty.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.thirty.R
import umu.kaoq0003.thirty.data.Result
import java.util.*

/**
 * Author: Karl Öqvist
 *
 * Adapter for ListView in ResultActivity
 * - Takes a list of Results of length 10 as input
 */

class ResultAdapter(context: Context, resultList: ArrayList<Result>?) : BaseAdapter() {

    private val mContext: Context = context

    private val results = resultList

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(mContext)
        val row = layoutInflater.inflate(R.layout.result_item, viewGroup, false)

        val methodText = row.findViewById<TextView>(R.id.card_method_variable_textview)
        val pointText = row.findViewById<TextView>(R.id.card_points_variable_textview)
        methodText.text = results?.get(position)?.method.toString()
        pointText.text = results?.get(position)?.points.toString()
        return row
    }

    override fun getCount(): Int {
        return 10
    }

    override fun getItem(p0: Int): Any {
        return "not implemented"
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


}