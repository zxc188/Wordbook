package com.example.fragment.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.fragment.R;
import com.example.fragment.adapter.MyItemRecyclerViewAdapter;
import com.example.fragment.dao.WordDBUtil;
import com.example.fragment.view.My_Recy;
import com.example.fragment.word.words;

import java.util.LinkedList;


public class ItemFragment extends Fragment {
    private WordDBUtil wordDBUtil;
    private LinkedList<words> linkedList;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private My_Recy recyclerView;
    private static MyItemRecyclerViewAdapter adapter;


    public ItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ItemFragment newInstance(int columnCount) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wordDBUtil = new WordDBUtil(getActivity());
        linkedList = wordDBUtil.getall();
        Log.i("aisdfokdkoa", "aijdiri");
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (My_Recy) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adapter = new MyItemRecyclerViewAdapter(linkedList, mListener);
            recyclerView.setAdapter(adapter);
            recyclerView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    menu.add(0, 1, 0, "修改");
                    menu.add(0, 2, 0, "删除");
                }
            });
        }
        return view;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int poss = menuInfo.position;
        Log.i("ajdifj", poss+"");
        final View view = getActivity().getLayoutInflater().inflate(R.layout.add, null);
        switch (item.getItemId()) {
            case 1:
                linkedList = wordDBUtil.getall();
                ((TextView) view.findViewById(R.id.ed1)).setText(linkedList.get(poss).getId());
                final String old = linkedList.get(poss).getId();
                ((TextView) view.findViewById(R.id.ed2)).setText(linkedList.get(poss).getChinese());
                ((TextView) view.findViewById(R.id.ed3)).setText(linkedList.get(poss).getExample());
                Log.i("change", poss + "");
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("修改")
                        .setView(view)
                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String idnew = ((TextView) view.findViewById(R.id.ed1)).getText().toString();
                                String chinew = ((TextView) view.findViewById(R.id.ed2)).getText().toString();
                                String examnew = ((TextView) view.findViewById(R.id.ed3)).getText().toString();
                                /*
                                * 对数据库做操作
                                * */
                                wordDBUtil.change(old,idnew, chinew, examnew);
                                linkedList = wordDBUtil.getall();
                                reflash(linkedList);
                            }
                        }).show();
                break;
            case 2:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                builder2.setTitle("删除")
                        .setMessage("你确定要删除吗")
                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                linkedList = wordDBUtil.getall();
                                String id = linkedList.get(poss).getId();
                                /*
                                 * 从数据库删除数据
                                 * */
                                wordDBUtil.delete(id);
                                Log.i("delete", poss + "");
                                linkedList = wordDBUtil.getall();
                                reflash(linkedList);
                            }
                        }).show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public static void reflash(LinkedList<words> linkedList) {
        adapter.setmValues(linkedList);
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(words item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        wordDBUtil.des();
    }
}
