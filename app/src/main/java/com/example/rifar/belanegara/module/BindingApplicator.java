package com.example.rifar.belanegara.module;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by asus on 8/15/2017.
 */

public class BindingApplicator {
    private Object mViewModel;
    private Class mViewModelType;
    private @LayoutRes int mLayout;
    private ArrayList<MemberSubscriber> mMemberSubscribers = new ArrayList<>();
    private Stack<Integer> mDepthStack = new Stack<>();
    private HashMap<String, ArrayList<BehaviorSubscriber>> mBehaviorSubscribers = new HashMap<>();
    private HashMap<String, Behavior> mBehaviors = new HashMap<>();
    private ArrayList<BindableList> mSourceSubscriber = new ArrayList<>();

    private static final String NAMESPACE = "http://schemas.android.com/apk/res-auto";
    private static final String ATTR_NAME = "binding";
    private static final Pattern REGEX = Pattern.compile("(\\{.[^\\{\\}]+\\})(;|$)");
    private static final Integer[] CAST_HELPER = new Integer[0];

    public BindingApplicator() {

    }

    public View applyBinding(Activity activity, @LayoutRes int layout, Object viewModel) {
        activity.setContentView(layout);
        final View view =((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        return applyBinding(view, layout, viewModel);
    }

    public View applyBinding(LayoutInflater inflater, ViewGroup container, @LayoutRes int layout, Object viewModel) {
        View view = inflater.inflate(layout, container, false);
        return applyBinding(view, layout, viewModel);
    }

    public View applyBinding(View view, @LayoutRes int layout, Object viewModel) {
        mViewModel = viewModel;
        mLayout = layout;
        mViewModelType = viewModel.getClass();
        final XmlPullParser parser = view.getContext().getResources().getLayout(layout);

        if(BindingContext.hasCached(mLayout, mViewModelType)) {
            try {
                BindingContext.applyBindingCache(this, view, layout, viewModel);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                while(parser.getEventType() != XmlPullParser.START_TAG) {
                    parser.next();
                }
                mDepthStack.add(0);
                mapBinding(parser, view);
            } catch (XmlPullParserException | IOException | IllegalAccessException | NoSuchFieldException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return view;
    }

    public void removeBinding() {
        for(MemberSubscriber subscriber : mMemberSubscribers)
            subscriber.member.removeBinding(subscriber.expr);
        for(BindableList list : mSourceSubscriber)
            list.detachAdapter();
        mBehaviors.clear();
        mBehaviorSubscribers.clear();
        mMemberSubscribers.clear();
        mSourceSubscriber.clear();
    }

    public void applyBehavior(Class<? extends Behavior> cls, String name) {
        try {
            handleBehavior(cls, name, null);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void applyBehavior(Class<? extends Behavior> cls, String name, Object depedency) {
        try {
            handleBehavior(cls, name, depedency);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void handleBehavior(Class<? extends Behavior> cls, String name, Object depedency) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        Behavior behavior = BindingContext.hasBehaviorCached(cls);
        if (behavior == null) {
            if(cls.getEnclosingClass() != null && depedency != null) {
                Constructor ctor = cls.getDeclaredConstructor(cls.getEnclosingClass());
                ctor.setAccessible(true);
                behavior = (Behavior) ctor.newInstance(depedency);
            }
            else
                behavior = cls.newInstance();

            BindingContext.registerBehavior(cls, behavior);
        }

        mBehaviors.put(name, behavior);
        if(mBehaviorSubscribers.containsKey(name)) {
            for(BehaviorSubscriber subscriber : mBehaviorSubscribers.get(name))
                behavior.applyBehavior(subscriber.view, subscriber.selector);
        }
    }

    private void mapBinding(XmlPullParser parser, View view) throws IOException, XmlPullParserException, NoSuchFieldException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
        if(parser.getEventType() == XmlPullParser.END_DOCUMENT)
            return;

        String clsName = parser.getName();
        if(view.getClass().getName().contains(clsName)) {
            String str = parser.getAttributeValue(NAMESPACE, ATTR_NAME);

            if(str != null) {
                Matcher matcher = REGEX.matcher(str);
                while(matcher.find()) {
                    String[] vals = matcher.group(1).replaceAll("[{}]", "").split(" ");

                    if(!vals[0].equals("behavior")) {
                        Field field = resolveMember(vals[1]);
                        Object src = field.get(mViewModel);
                        if(vals[0].equals("adapter") && src instanceof BindableList) {
                            int layout;
                            if(vals.length == 3) {
                                Context context = view.getContext();
                                layout = context.getResources().getIdentifier(vals[2], null,
                                        vals[2].contains("android:")? "android" : context.getPackageName());
                                bindAdapter(field, view, layout);
                            }
                            else
                                bindAdapter(field, view);
                        }
                        else if(src instanceof BindableMember) {
                            if (vals.length == 3)
                                bindMember(field, view, vals[0], BindingMode.valueOf(vals[2]));
                            else
                                bindMember(field, view, vals[0]);

                        }
                    }
                    else {
                        String selector = vals.length == 3 ? vals[2] : null;
                        subscribeBehavior(vals[1], view, selector);
                        BindingContext.registerCache(mLayout, mViewModelType, vals[1], mDepthStack.toArray(CAST_HELPER), selector);
                    }
                }
            }

            if(view instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) view;
                mDepthStack.add(0);
                for(int i = 0; i < vg.getChildCount(); i++) {
                    parser.nextTag();
                    if(parser.getEventType() == XmlPullParser.END_TAG)
                        parser.nextTag();

                    View child = vg.getChildAt(i);
                    mapBinding(parser, child);

                    int k = mDepthStack.pop();
                    mDepthStack.add(k + 1);
                }
                mDepthStack.pop();
                parser.nextTag();
            }
        }
    }

    private Field resolveMember(String str) throws NoSuchFieldException, IllegalAccessException {
        String[] fields = str.split("\\.");
        Field field = null;
        Class cls;
        Object model = null;
        for(String name : fields) {
            model = model == null? mViewModel : field.get(model);
            cls = field == null? mViewModelType : ((BindableMember) model).getType();
            field = cls.getField(name);
        }
        return field;
    }

    private void bindAdapter(Field field, View view) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        bindAdapter(field, view, 0);
    }

    private void bindAdapter(Field field, View view, @LayoutRes int layout) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        BindableList list = (BindableList) field.get(mViewModel);
        Class viewType = view.getClass();
        Method setter = BindingContext.hasAdapterCached(viewType);
        if(setter == null) {
            for(Method m : viewType.getMethods()) {
                if(m.getName().equals("setAdapter")) {
                    setter = m;
                    BindingContext.registerAdapter(viewType, setter);
                    break;
                }
            }
        }
        BindableAdapter adapter = new BindableAdapter(view.getContext(), list, layout);
        setter.invoke(view, adapter);
        BindingContext.registerCache(mLayout, mViewModelType, layout, field, view.getClass(), setter, mDepthStack.toArray(CAST_HELPER));
        list.atttachAdapter(adapter);
        subscribeSource(list);
    }

    private void bindMember(Field field, View view, String attrName) throws IllegalAccessException {
        bindMember(field, view, attrName, BindingMode.OneWay);
    }

    private void bindMember(Field field, View view, String attrName, BindingMode mode) throws IllegalAccessException {
        BindableMember member =  (BindableMember)field.get(mViewModel);
        if(member != null) {
            BindingExpression expr = new BindingExpression(member.getType(), view, attrName, mode);
            subscribeBinding(member, expr);
            member.createBinding(expr);

            BindingContext.registerCache(mLayout, mViewModelType, field, member.getType(),
                    attrName, mDepthStack.toArray(CAST_HELPER), mode);
        }
    }

    void subscribeBinding(BindableMember member, BindingExpression expr) {
        MemberSubscriber subscriber = new MemberSubscriber();
        subscriber.expr = expr;
        subscriber.member = member;
        mMemberSubscribers.add(subscriber);
    }

    void subscribeSource(BindableList list) {
        mSourceSubscriber.add(list);
    }

    void subscribeBehavior(String name, View view, String selector) throws InvocationTargetException, IllegalAccessException {
        if(!mBehaviorSubscribers.containsKey(name))
            mBehaviorSubscribers.put(name, new ArrayList<>());

        BehaviorSubscriber sub = new BehaviorSubscriber();
        sub.view = view;
        sub.selector = selector;
        mBehaviorSubscribers.get(name).add(sub);

        if(mBehaviors.containsKey(name))
            mBehaviors.get(name).applyBehavior(view, selector);
    }

    private class MemberSubscriber {
        BindableMember member;
        BindingExpression expr;
    }

    private class BehaviorSubscriber {
        View view;
        String selector;
    }
}
