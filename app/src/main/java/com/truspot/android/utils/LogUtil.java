package com.truspot.android.utils;

import android.util.Log;
import com.truspot.android.constants.Constants;
import com.truspot.android.interfaces.ILoggable;
import com.truspot.android.models.LogRecordPair;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Utility methods used to log application for better debugging.  
 * */
public class LogUtil {

    public static void log(String tag, String msg) {
        log(Log.INFO, tag, msg);
    }

    public static void log(int type, String tag, String msg) {
        if (Constants.IS_DEBUG) {
            switch (type) {
                case Log.INFO : {
                    Log.i(tag, msg);

                    break;
                }
                case Log.ERROR : {
                    Log.e(tag, msg);

                    break;
                }
            }
        }
    }

/*
	public static void log(int type, String tag, String msg) {
        ArrayList<String> strings = null;

        if (Util.isStringNotNull(msg)) {
            strings = splitString(msg, 4000);
        }

		if (Constants.IS_DEBUG) {
			switch (type) {
				case Log.INFO : {
                    if (strings != null) {
                        int size = strings.size();

                        if (size > 1) {
                            for (int i = 0; i < size; i++) {
                                Log.i(tag + " " + i, strings.get(i));
                            }
                        } else {
                            Log.i(tag, msg);
                        }
                    } else {
                        Log.i(tag, "null");
                    }


					break;
				}
				case Log.ERROR : {
                    if (strings != null) {
                        int size = strings.size();

                        if (size > 1) {
                            for (int i = 0; i < size; i++) {
                                Log.e(tag + " " + i, strings.get(i));
                            }
                        } else {
                            Log.e(tag, msg);
                        }
                    } else {
                        Log.e(tag, "null");
                    }

					break;
				}
			}
		}
	}
*/

	public static void logList(int type, String tag, String msg, List<? extends ILoggable> list) {
		if (list != null && !list.isEmpty()) {
			for (ILoggable item : list) {
				log(type, tag, msg + " " + item.toLog());
			}
		}
	}

	public static void logArray(int type, String tag, String msg, ILoggable[] arr) {
		if (arr != null && arr.length > 0) {
			for (ILoggable item : arr) {
				log(type, tag, msg + " " + item.toLog());
			}
		}
	}

	public static String getLog(LogRecordPair[] logRecordPairs) {
		return getLog(logRecordPairs, true);
	}
	
	public static String getLog(LogRecordPair[] logRecordPairs, boolean withBrackets) {
		String result;

		result = "";
		
		if (logRecordPairs != null && logRecordPairs.length > 0) {
			if (withBrackets) {
				result = "{";				
			}
			
			for (LogRecordPair logRecPair : logRecordPairs) {
				if (!result.equals("{")) {
					result += ", ";
				}

				result += "\"" + logRecPair.getName()  + "\" : ";

                String strValue = logRecPair.getStrValue();

                strValue = strValue == null ? "" : strValue;

				if (logRecPair.isPutQuotesAroundValue()) {


					result += "\"" + strValue + "\"";
				} else {
					result += strValue;
				}
			}

			if (withBrackets) {
				result += "}";
			}
		}

		return result;
	}

	public static String getLog(LogRecordPair[][] logRecordPairs) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0, z = logRecordPairs.length; i < z; i++) {
			sb.append(getLog(logRecordPairs[i], false) + (i < z - 1 ? "," : ""));
		}

		return sb.toString();
	}
	
	public static String getLogFromList(Collection<? extends ILoggable> list) {
		String result;

		result = "[";

		if (list == null || list.isEmpty()) {
			result += "null";
		} else {
			for (ILoggable log : list) {
				result += log.toLog() + ", ";
			}

			if (!result.equals("[")) {
				result = result.substring(0, result.length() - 2);
			}
		}

		result += "]";

		return result;
	}

	public static String getLogFromStringList(List<String> list) {
		String result;

		result = "[";

		if (list == null || list.isEmpty()) {
			result += "null";
		} else {
			for (String log : list) {
				result += log + ", ";
			}

			if (!result.equals("[")) {
				result = result.substring(0, result.length() - 2);
			}
		}

		result += "]";

		return result;
	}

	public static String getLogFromLongList(List<Long> list) {
		String result;

		result = "[";

		if (list == null || list.isEmpty()) {
			result += "null";
		} else {
			for (Long log : list) {
				result += log + ", ";
			}

			if (!result.equals("[")) {
				result = result.substring(0, result.length() - 2);
			}
		}

		result += "]";

		return result;
	}
	
	public static String getLogFromBooleanArray(boolean[] arr) {
		String result;

		result = "[";

		if (arr == null || arr.length == 0) {
			result += "null";
		} else {
			for (boolean flag : arr) {
				result += String.valueOf(flag) + ", ";
			}

			if (!result.equals("[")) {
				result = result.substring(0, result.length() - 2);
			}
		}

		result += "]";

		return result;
	}
	
	public static String getLogFromStringArray(String[] arr) {
		String result;

		result = "[";

		if (arr == null || arr.length == 0) {
			result += "null";
		} else {
			for (String str : arr) {
				result += "'" + str + "'" +", ";
			}

			if (!result.equals("[")) {
				result = result.substring(0, result.length() - 2);
			}
		}

		result += "]";

		return result;
	}

	public static String getLogFromArray(ILoggable[] arr) {
		String result;

		result = "[";

		if (arr == null || arr.length == 0) {
			result += "null";
		} else {
			for (ILoggable obj : arr) {
				result += obj.toLog() + ", ";
			}

			if (!result.equals("[")) {
				result = result.substring(0, result.length() - 2);
			}
		}

		result += "]";

		return result;
	}

	// private methods
    /**
     * Divides a string into chunks of a given character size.
     *
     * @param text                  String text to be sliced
     * @param sliceSize             int Number of characters
     * @return  ArrayList<String>   Chunks of strings
     */
    public static ArrayList<String> splitString(String text, int sliceSize) {
        ArrayList<String> textList = new ArrayList<String>();
        String aux;
        int left = -1, right = 0;
        int charsLeft = text.length();
        while (charsLeft != 0) {
            left = right;
            if (charsLeft >= sliceSize) {
                right += sliceSize;
                charsLeft -= sliceSize;
            }
            else {
                right = text.length();
                aux = text.substring(left, right);
                charsLeft = 0;
            }
            aux = text.substring(left, right);
            textList.add(aux);
        }
        return textList;
    }
}