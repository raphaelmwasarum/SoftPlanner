package com.raphjava.softplanner.data.interfaces;

public interface IOService
{
    /** Creates the file and its parent directories if it doesn't already exist.
     * @param directory the subject directory.
     * @return true if the file exists or its creation is successful.
     */
    boolean ensureExistence(String directory);

    /** Overwrites to file. By default, this method creates the file if it doesn't exist.
     * @param filePath
     * @param data
     * @param failIfAbsent doesn't create the file if it doesn't exist.
     * @return true if writing was successful, false otherwise.
     */
    boolean writeToFile(String filePath, String data, boolean... failIfAbsent);

}