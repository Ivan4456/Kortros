package ru.mysmartflat.kortros;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewPager_Bilder_Adapter extends PagerAdapter{

    private Context context;
    private LayoutInflater layoutInflater;
    private Integer [] images = {R.drawable.icon_coin,R.drawable.icon_hole,R.drawable.icon_diagram,R.drawable.icon_modern};
    private String [] headers = {"Чистая прибыль","Эффективность и рентабельность","Устойчивое развитие","Модернизация продукта"};
    private String [] texts = {"Стратегические объекты позволяют обеспечить стабильные денежные потоки в долгосрочной перспективе",
                               "Повышать эффективность и рентабельность за счет реализации точечных высоко-маржинальных проектов",
                               "Развивать девелоперскую деятельность в наиболее привлекательных и быстрорастущих регионах России",
                               "Качественный конкурентный продукт, отвечающий запросам потребителя, индивидуально для каждого проекта"};


    public ViewPager_Bilder_Adapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view2 = layoutInflater.inflate(R.layout.layout_pager2,null);

        ImageView imageView = (ImageView) view2.findViewById(R.id.imageView_pager_ID);
        imageView.setImageResource(images[position]);

        TextView TextView_Page_headers = (TextView) view2.findViewById(R.id.textView_pager_header_ID);
        TextView_Page_headers.setText(headers[position]);


        TextView TextView_Page = (TextView) view2.findViewById(R.id.textView_pager_ID);
        TextView_Page.setText(texts[position]);


        ViewPager vp = (ViewPager) container;
        vp.addView(view2,0);
        return view2;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }
}
