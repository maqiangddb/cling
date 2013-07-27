package com.some;

import android.util.Log;

import org.fourthline.cling.support.contentdirectory.AbstractContentDirectoryService;
import org.fourthline.cling.support.contentdirectory.ContentDirectoryException;
import org.fourthline.cling.support.contentdirectory.DIDLParser;
import org.fourthline.cling.support.model.BrowseFlag;
import org.fourthline.cling.support.model.BrowseResult;
import org.fourthline.cling.support.model.DIDLContent;
import org.fourthline.cling.support.model.SortCriterion;
import org.fourthline.cling.support.model.container.Container;
import org.fourthline.cling.support.model.item.Item;

/**
 * Created by mqddb on 13-6-3.
 */
public class ContentDirectoryService extends AbstractContentDirectoryService {


    @Override
    public BrowseResult browse(String objectID, BrowseFlag browseFlag,
                               String filter, long firstResult,
                               long maxResults, SortCriterion[] orderby) throws ContentDirectoryException {
        Log.i("", "BrowseResult==objectId:"+objectID+" browseFlag:"+browseFlag);
        try {
        DIDLContent didl = new DIDLContent();

        ContentNode contentNode = ContentTree.getNode(objectID);

        Log.i("", "someone's browsing id:"+objectID);
        if (contentNode.isItem()) {
            didl.addItem(contentNode.getItem());
             return new BrowseResult(new DIDLParser().generate(didl),1,1);
        } else {
            if (browseFlag == BrowseFlag.METADATA) {
                Log.i("", "returning metadata of container:"+contentNode.getContainer().getTitle());
                return new BrowseResult(new DIDLParser().generate(didl),1,1);
            } else {
                for (Container container : contentNode.getContainer().getContainers()) {
                    didl.addContainer(container);
                    Log.i("", "getting child container:" + container.getTitle());
                }
                for (Item item : contentNode.getContainer().getItems()) {
                    didl.addItem(item);
                    Log.i("", "getting child item:"+item.getTitle());
                }
                return new BrowseResult(new DIDLParser().generate(didl),
                        contentNode.getContainer().getChildCount(),
                        contentNode.getContainer().getChildCount());
            }
        }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
