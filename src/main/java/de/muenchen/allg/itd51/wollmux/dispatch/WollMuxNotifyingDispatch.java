package de.muenchen.allg.itd51.wollmux.dispatch;

import java.util.Arrays;

import com.sun.star.beans.PropertyValue;
import com.sun.star.frame.DispatchResultEvent;
import com.sun.star.frame.DispatchResultState;
import com.sun.star.frame.XDispatch;
import com.sun.star.frame.XDispatchResultListener;
import com.sun.star.frame.XFrame;
import com.sun.star.frame.XNotifyingDispatch;
import com.sun.star.util.URL;

import de.muenchen.allg.afid.UNO;

/**
 * A dispatch executed by WollMux. Registered Listeners are notified after completion.
 */
public abstract class WollMuxNotifyingDispatch extends WollMuxDispatch
    implements XNotifyingDispatch, DispatchHelper
{
  protected XDispatchResultListener listener;

  protected PropertyValue[] props;

  /**
   * Creates a new dispatch executed by WollMux.
   *
   * @param origDisp
   *          The original dispatch.
   * @param origUrl
   *          The original command URL.
   * @param frame
   *          The frame of the command.
   */
  public WollMuxNotifyingDispatch(XDispatch origDisp, URL origUrl, XFrame frame)
  {
    super(origDisp, origUrl, frame);
  }

  @Override
  public void dispatchOriginal()
  {
    if (origDisp != null)
    {
      int index = props.length;
      PropertyValue[] newProps = Arrays.copyOf(props, props.length + 1);
      newProps[index] = new PropertyValue();
      newProps[index].Name = "SynchronMode";
      newProps[index].Value = false;
      if (listener == null)
      {
        origDisp.dispatch(origUrl, newProps);
      } else
      {
        final XNotifyingDispatch nd = UNO.XNotifyingDispatch(origDisp);
        nd.dispatchWithNotification(origUrl, newProps, listener);
      }
    }
  }

  @Override
  public void dispatchFinished(boolean success)
  {
    if (listener != null)
    {
      final DispatchResultEvent dre = new DispatchResultEvent();
      dre.Source = this;
      if (success)
      {
        dre.State = DispatchResultState.SUCCESS;
      } else
      {
        dre.State = DispatchResultState.FAILURE;
      }
      listener.dispatchFinished(dre);
    }
  }
}
