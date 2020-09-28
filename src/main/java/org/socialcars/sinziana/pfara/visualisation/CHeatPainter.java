/*
 * This file is part of the mesoscopic traffic simulation PFaRA of Clauthal University of
 * Technology-Mobile and Enterprise Computing aswell as SocialCars Research Training Group.
 *  Copyright (c) 2017-2021 Sinziana-Maria Sebe (sms14@tu-clausthal.de)
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU
 *  General Public License as  published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 *  PURPOSE.  See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this program. If not, see http://www.gnu.org/licenses/
 */

package org.socialcars.sinziana.pfara.visualisation;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * heat painting class
 * like the route painter but with varying colours based on the frequency of use of the edges
 */
public class CHeatPainter implements Painter<JXMapViewer>
{
    private List<List<GeoPosition>> m_routes;
    private HashMap<GeoPosition, Integer> m_values;
    private boolean m_antialias = true;
    private HashMap<GeoPosition, Color> m_heat;

    /**
     * ctor
     * @param p_tracks the list of routes
     */
    public CHeatPainter( final List<List<GeoPosition>> p_tracks )
    {
        m_routes = p_tracks;
        m_values = new HashMap<>();
        p_tracks.forEach( l -> l.forEach( i -> m_values.put( i, m_values.getOrDefault( i, 0 ) + 1 ) ) );
        m_heat = new HashMap<>();
        final Integer l_max = m_values.entrySet().stream().max( Map.Entry.comparingByValue() ).get().getValue();
        m_values.entrySet().forEach( p -> m_heat.put( p.getKey(), EColourMap.PLASMA.apply( p.getValue(), l_max ) ) );
    }

    /**
     * paint function
     * @param p_graphics the graphics object
     * @param p_viewer the viewer object
     * @param p_width the width of the window
     * @param p_height the height of the window
     */
    @Override
    public void paint( final Graphics2D p_graphics, final JXMapViewer p_viewer, final int p_width, final int p_height )
    {
        final Graphics2D l_graphics = (Graphics2D) p_graphics.create();

        // convert from viewport to world bitmap
        final Rectangle l_rect = p_viewer.getViewportBounds();
        l_graphics.translate( -l_rect.x, -l_rect.y );

        if ( m_antialias )
            l_graphics.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );

        l_graphics.setStroke( new BasicStroke( 3 ) );
        drawHeat( l_graphics, p_viewer );

        l_graphics.dispose();

    }

    /**
     * draws the heat
     * @param p_graphics the ggraphics object
     * @param p_map the map
     */
    private void drawHeat( final Graphics2D p_graphics, final JXMapViewer p_map )
    {
        m_routes.forEach( i ->
        {
            int l_lastx = 0;
            int l_lasty = 0;
            boolean first = true;


            for ( final GeoPosition l_gp : i )
            {
                final Point2D l_pt = p_map.getTileFactory().geoToPixel( l_gp, p_map.getZoom() );
                if ( first ) first = false;
                else
                {
                    p_graphics.setColor( m_heat.get( l_gp ) );
                    p_graphics.drawLine( l_lastx, l_lasty, (int) l_pt.getX(), (int) l_pt.getY() );
                }
                l_lastx = (int) l_pt.getX();
                l_lasty = (int) l_pt.getY();
            }
        } );
    }

    public HashMap<GeoPosition, Integer> getValues()
    {
        return m_values;
    }
}
