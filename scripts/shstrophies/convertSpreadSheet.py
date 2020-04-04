# pip install pandas
# pip install jupyter
# pip install openpyxl
# pip install xlrd
import glob
from io import BytesIO
import requests
import pandas as pd
import re
import os



def append_df_to_excel(df, filename, sheet_name='Sheet1', startrow=None,
                       truncate_sheet=False,
                       **to_excel_kwargs):

    print('append_df_to_excel: filname=' + str(filename) + ', sheet_name=' + str(sheet_name) + ', startrow=' + str(startrow) + ', truncate_sheet=' + str(truncate_sheet) + ', to_excel_kwargs=' + str(to_excel_kwargs))

    """
    Append a DataFrame [df] to existing Excel file [filename]
    into [sheet_name] Sheet.
    If [filename] doesn't exist, then this function will create it.

    Parameters:
      filename : File path or existing ExcelWriter
                 (Example: '/path/to/file.xlsx')
      df : dataframe to save to workbook
      sheet_name : Name of sheet which will contain DataFrame.
                   (default: 'Sheet1')
      startrow : upper left cell row to dump data frame.
                 Per default (startrow=None) calculate the last row
                 in the existing DF and write to the next row...
      truncate_sheet : truncate (remove and recreate) [sheet_name]
                       before writing DataFrame to Excel file
      to_excel_kwargs : arguments which will be passed to `DataFrame.to_excel()`
                        [can be dictionary]

    Returns: None
    """
    from openpyxl import load_workbook

    # ignore [engine] parameter if it was passed
    if 'engine' in to_excel_kwargs:
        to_excel_kwargs.pop('engine')

    writer = pd.ExcelWriter(filename, engine='openpyxl')

    try:
        # try to open an existing workbook
        writer.book = load_workbook(filename)

        # get the last row in the existing Excel sheet
        # if it was not specified explicitly
        if startrow is None and sheet_name in writer.book.sheetnames:
            startrow = writer.book[sheet_name].max_row

        # truncate sheet
        if truncate_sheet and sheet_name in writer.book.sheetnames:
            # index of [sheet_name] sheet
            idx = writer.book.sheetnames.index(sheet_name)
            # remove [sheet_name]
            writer.book.remove(writer.book.worksheets[idx])
            # create an empty sheet [sheet_name] using old index
            writer.book.create_sheet(sheet_name, idx)

        # copy existing sheets
        writer.sheets = {ws.title: ws for ws in writer.book.worksheets}
    except FileNotFoundError:
        # file does not exist yet, we will create it
        pass

    if startrow is None:
        startrow = 0

    # write out the new sheet
    df.to_excel(writer, sheet_name, startrow=startrow, **to_excel_kwargs)

    # save the workbook
    writer.save()


def convertDataFrame(inputBaseFileName, inputSheetName, frame, theTitle, theUrl, lastSheet=None, exportFile='export.xlsx'):
    print("convertDataFrame inputBaseFileName=" + inputBaseFileName + ", inputSheetName=" + inputSheetName + ", title=" + theTitle + ", url=" + theUrl + ", lastSheet=" + str(lastSheet))
    # print("frame=" + frame.to_string())
    if(not theTitle):
        print("title is empty, skip")
        return

    if (theTitle == 'Year'):
        return
    dfa = frame[['Year', theTitle]].assign(title=theTitle).copy()
    # print("dfa=" + dfa.to_string())
    # fill any Nan Year with the previous row's non-Nan Year value
    dfb = dfa['Year'].fillna(method='ffill')
    # print("dfb=" + dfb.to_string())
    dfa.loc[dfb.index, 'Year'] = dfb
    # print("dfa=" + dfa.to_string())
    dfc = dfa[['Year', theTitle]].assign(title=theTitle).dropna().copy()
    # print("dfc=" + dfc.to_string())
    dfc.rename(columns={theTitle: 'Player_Name'}, inplace=True)
    # print("dfc=" + dfc.to_string())
    dfd = dfc[['Year', 'Player_Name']].assign(Trophy_Title=theTitle).copy()
    dfe = dfd.replace({'Year': r'[/-].*'}, {'Year': ''}, regex=True)[['Year', 'Trophy_Title', 'Player_Name']].copy()

    dff = dfe[['Year', 'Trophy_Title', 'Player_Name']].assign(Trophy_Image_URI=theUrl).copy()
    dfg = dff[['Year', 'Trophy_Title', 'Trophy_Image_URI', 'Player_Name']].copy()
    # dfg.drop(dfg.columns[0], axis=1, inplace=True)

    # print("converted dfg=" + dfg.to_string())

    outputSheetName = inputBaseFileName
    print('outputSheetName=' + outputSheetName)
    startRow=None
    header=True
    if(inputBaseFileName in lastSheet):
        header=False

    append_df_to_excel(dfg, exportFile, sheet_name=outputSheetName, startrow=startRow, header=header, index=False)

    theFileName = str(r'export_' + inputBaseFileName.replace(" ", "_") + '_' + theTitle.replace(" ", "_") + '.xlsx')
    # dfg.to_excel(theFileName, sheet_name=outputSheetName, index=False, header=True)
    return dfg


def getImageUrl(df, column, defaultUrl):
    imageUrl = defaultUrl
    firstRow = df[column].iloc[0]
    theType = type(firstRow)
    if (type(firstRow) == str):
        # print("first row=" + firstRow + " " + ",type=" + str(type(firstRow)))
        theFirstRow = str(firstRow)
        pattern = '^(http|https)://'
        if (re.match(pattern, theFirstRow)):
            imageUrl = firstRow
    return imageUrl


def convertExcelFile(inputBaseFileName, excelFile, exportFile):
    lastinputBaseFileName = {}
    # df = pd.read_excel(excelFile)
    xls = pd.ExcelFile(excelFile)
    for sheet_name in xls.sheet_names:
        print("*******input sheet=" + sheet_name)
        df = xls.parse(sheet_name)
        convertExcelSheet(inputBaseFileName, sheet_name, df, exportFile, lastinputBaseFileName)


def convertExcelSheet(inputBaseFileName, inputSheetName, df, exportFile, lastinputBaseFileName):
    df.columns = df.columns.to_series().apply(lambda x: x.strip())
    columns = df.columns.values
    newCol = 'Year'
    print("renaming first column to " + newCol)
    df.rename(columns={columns[0]: newCol}, inplace=True)
    df.columns = df.columns.to_series().apply(lambda x: x.strip())
    columns = df.columns.values
    for column in columns:
        print("column=" + column)
        if ((column == 'Unnamed: 0')):
            newCol = 'Year'
            print('renaming column=' + column + " to " + newCol)
            df.rename(columns={'Unnamed: 0': newCol}, inplace=True)
            column = 'Year'
        if (column != 'Year'):
            imageUrl = getImageUrl(df, column, 'https://TODO')
            print("imageUrl=" + imageUrl)
            convertDataFrame(inputBaseFileName, inputSheetName, df, column, imageUrl, lastinputBaseFileName, exportFile)
            lastinputBaseFileName[inputBaseFileName] = column

def findFilesInFolder(path, pathList, extension, subFolders = True):
    """  Recursive function to find all files of an extension type in a folder (and optionally in all subfolders too)

    path:        Base directory to find files
    pathList:    A list that stores all paths
    extension:   File extension to find
    subFolders:  Bool.  If True, find files in all subfolders under path. If False, only searches files in the specified folder
    """

    try:   # Trapping a OSError:  File permissions problem I believe
        for entry in os.scandir(path):
            if entry.is_file() and entry.path.endswith(extension):
                pathList.append(entry.path)
            elif entry.is_dir() and subFolders:   # if its a directory, then repeat process as a nested function
                pathList = findFilesInFolder(entry.path, pathList, extension, subFolders)
    except OSError:
        print('Cannot access ' + path +'. Probably a permissions error')

    return pathList

extension = ".xlsx"
exportFile='export' + extension
os.remove(exportFile) if os.path.exists(exportFile) else None

# to test
# convertSpreadSheetFromExcel('G. Golf', './data/Mr Torren_s initial data input/Fall Awards/G. Golf.xlsx', exportFile)

dir_name = r'./data'
pathList = []
pathList = findFilesInFolder(dir_name, pathList, extension, True)
print("all files=" + str(pathList))
for file in pathList:
    basename = os.path.basename(str(file))
    info = os.path.splitext(basename)
    baseFileName = info[0]
    print('************************************************************')
    print('************************************************************')
    print('************************************************************')
    print("file=" + str(file) + ", baseFileName=" + baseFileName)
    print('************************************************************')
    print('************************************************************')
    print('************************************************************')
    convertExcelFile(baseFileName, str(file), exportFile)

