package com.wireme.mediaserver;

import android.app.Activity;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.wireme.activity.ContentItem;

import org.fourthline.cling.model.action.ActionException;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.ErrorCode;
import org.fourthline.cling.support.contentdirectory.callback.Browse;
import org.fourthline.cling.support.model.BrowseFlag;
import org.fourthline.cling.support.model.DIDLContent;
import org.fourthline.cling.support.model.container.Container;
import org.fourthline.cling.support.model.item.Item;

import java.util.logging.Logger;

/**
 * Created by mqddb on 13-6-4.
 */
public class ContentBrowseActionCallback extends Browse {
    private static final Logger log = Logger.getLogger(ContentBrowseActionCallback.class
            .getName());

    private Service _service;
    private Container _container;
    private ArrayAdapter<ContentItem> _listAdapter;
    private Activity _activity;

    public ContentBrowseActionCallback(Activity activity, Service service,
                                       Container container, ArrayAdapter<ContentItem> listadapter) {
        this(service,container.getId(),BrowseFlag.DIRECT_CHILDREN);
        _activity = activity;
        _container = container;
        _listAdapter = listadapter;
    }

    public ContentBrowseActionCallback(Service service, String containerId, BrowseFlag flag) {
        super(service, containerId, flag);
        Log.e("MQ",""+service+","+containerId+","+flag);
        _service = service;
    }

    @Override
    public void received(final ActionInvocation actionInvocation, final DIDLContent didl) {
        log.fine("Received browse action DIDL descriptor, creating tree nodes");
        _activity.runOnUiThread(new Runnable() {
            public void run() {
                try {
                    _listAdapter.clear();
                    // Containers first
                    for (Container childContainer : didl.getContainers()) {
                        log.fine("add child container " + childContainer.getTitle());
                        _listAdapter.add(new ContentItem(childContainer, _service));
                    }
                    // Now items
                    for (Item childItem : didl.getItems()) {
                        log.fine("add child item" + childItem.getTitle());
                        _listAdapter.add(new ContentItem(childItem, _service));
                    }
                } catch (Exception ex) {
                    log.fine("Creating DIDL tree nodes failed: " + ex);
                    actionInvocation.setFailure(new ActionException(
                            ErrorCode.ACTION_FAILED,
                            "Can't create list childs: " + ex, ex));
                    failure(actionInvocation, null);
                }
            }
        });
    }

    @Override
    public void updateStatus(Status status) {

    }

    @Override
    public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {

    }
}
