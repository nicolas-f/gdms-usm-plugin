/**
 *
 * Gdms-USM is a library dedicated to multi-agent simulation for modeling urban sprawl.
 * It is based on the GDMS library. It uses the OrbisGIS renderer to display results.
 *
 * This version is developed at French IRSTV Institute and at LIENSs UMR 7266 laboratory
 * (http://lienss.univ-larochelle.fr/) as part of the VegDUD project, funded by the
 * French Agence Nationale de la Recherche (ANR) under contract ANR-09-VILL-0007.
 *
 * Gdms-USM is distributed under GPL 3 license. It is maintained by the "Atelier SIG" team of
 * the IRSTV Institute <http://www.irstv.fr/> CNRS FR 2488.
 *
 * Copyright (C) 2011-2012 IRSTV (FR CNRS 2488)
 *
 * Gdms-USM is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Gdms-USM is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Gdms-USM. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://trac.orbisgis.org/>
 * or contact directly:
 * info_at_ orbisgis.org
 */
package org.gdms.usm.view;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.gdms.data.DataSourceCreationException;
import org.gdms.data.NoSuchTableException;
import org.gdms.data.NonEditableDataSourceException;
import org.gdms.data.indexes.IndexException;
import org.gdms.driver.DriverException;
import org.gdms.usm.Step;
import org.orbisgis.view.background.BackgroundJob;
import org.orbisgis.progress.ProgressMonitor;

/**
 *
 * @author Thomas Salliou
 */
class ExecuteSimulation implements BackgroundJob {

    private Step simulation;
    
    public ExecuteSimulation(Step s) {
        simulation = s;
    }

    @Override
    public void run(ProgressMonitor pm) {
        try {
            simulation.wholeSimulation();
        } catch (NoSuchTableException ex) {
            Logger.getLogger(ExecuteSimulation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataSourceCreationException ex) {
            Logger.getLogger(ExecuteSimulation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DriverException ex) {
            Logger.getLogger(ExecuteSimulation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NonEditableDataSourceException ex) {
            Logger.getLogger(ExecuteSimulation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ExecuteSimulation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IndexException ex) {
            Logger.getLogger(ExecuteSimulation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getTaskName() {
        return "USM Simulation";
    }
    
}
