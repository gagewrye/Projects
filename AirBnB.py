"""
    Author: Gage Wrye

    Date: November 24, 2021

    For this assignment I explore the following questions:
        - Is there any correlation between the price of a listing and the overall satisfaction?
        - Do hosts typically have multiple listings at the same time?
        - How do the prices of a rental change over time?
        - How different are the prices in different neighborhoods?
    I also create a function that plots the results of one of the other functions in order
    to visualize the results.
"""

from scipy.stats.stats import spearmanr
import matplotlib.pyplot as plt


def price_satisfaction(filename):
    """
    This function takes a file and creates a list of lists (of float)
    where each sublist contains exactly two items:
    the price and the overall_satisfaction.

    The houses not rented or reviewed are not included in the list

    :param filename: string containing the name of a file
    :return main_list: list with sublists like [price, satisfaction]
    """
    with open(filename, "r", encoding="utf-8") as file_in:
        main_list = []
        # Checks to see the position of desired categories
        header = file_in.readline()
        categories = header.split(',')
        review_index = categories.index("reviews")
        price_index = categories.index('price')
        rating_index = categories.index('overall_satisfaction')

        for line in file_in:
            stat_list = line.split(",")
            if len(stat_list) == len(categories):
                review = stat_list[review_index]
                if review != "reviews" and int(review) > 0:
                    # check if indices exist and add them to the sublist
                    if stat_list[price_index] != "" and stat_list[rating_index] != "":
                        sublist = [float(stat_list[price_index]), float(stat_list[rating_index])]
                        main_list.append(sublist)
        return main_list


def correlation(l):
    """
    This function takes a list that is the output of price_satisfaction and uses
    Spearman's rank correlation to determine if the prices and ratings are related.

    :param l: list consisting of pairs of price and rating
    :return: correlation between the price and the overall satisfaction
    """
    price = []
    rating = []
    for list_num in range(len(l)):
        price.append(float(l[list_num][0]))
        rating.append(float(l[list_num][1]))
    result = spearmanr(price, rating)

    return result.correlation, result.pvalue


def host_listings(filename):
    """
    This function takes a string containing the name of a file.
    It creates and returns a dictionary where the keys are host_ids (int)
    and the values are a list of room_ids (int) associated with that host.

    :param filename: input Airbnb file
    :return host_dict: dictionary of host_ids and corresponding room_ids
    """
    with open(filename, "r", encoding="utf-8") as file_in:
        host_dict = {}
        header = file_in.readline()
        categories = header.split(',')
        host_index = categories.index("host_id")
        room_index = categories.index('room_id')

        for line in file_in:
            stats = line.split(',')
            if len(stats) == len(categories):
                host_id = stats[host_index]
                room_id = stats[room_index]
                if room_id != "room_id" and room_id != "" and host_id != "":
                    if host_id in host_dict:
                        host_dict[host_id].append(int(room_id))
                    else:
                        host_dict[host_id] = [room_id]
        return host_dict


def num_listings(d):
    """
    This function takes a dictionary that is in the format of the one
    returned by host_listings (Function 2). It returns a list l where l[i] is the
    number of hosts that have exactly i listings.

    :param d: dictionary from host_listings
    :return l: list containing hows manys hosts have the index number of listings
    """
    l = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
    slots = 10
    for key in d:
        number = len(d[key])
        if number > slots:
            for i in range((number - slots)):
                l.append(0)
            slots = number
        l[number] = l[number] + 1
    return l


def room_prices(filename_list, roomtype):
    """
    This function checks the dates in the file names and runs analysis
    on them in order of earliest to latest.
    The function returns a dictionary where the keys are room_ids (int)
    and the values are a list of the prices (float) for that listing over time,
    from the oldest data to the most recent.

    :param filename_list: list of Airbnb files
    :param roomtype: string designating the type of room
    :return price_dict: dictionary with room_ids and the corresponding list of prices over time
    """
    price_dict = {}
    date_list = []
    # finds the dates for each list
    for file in filename_list:
        dates = file.strip('.cvs')
        year = dates[-10:-6]
        month = dates[-5:-3]
        day = dates[-2:]
        date = int(year + month + day)
        date_list.append(date)
    while len(date_list) > 0:
        for i in range(len(date_list)):
            # check for the highest date
            if date_list[i] == min(date_list):
                # match that earliest date index with corresponding file and remove both
                file_name = filename_list[i]
                filename_list.remove(filename_list[i])
                date_list.remove(min(date_list))
                # Runs analysis on chosen file
                with open(file_name, "r", encoding="utf-8") as file_in:
                    header = file_in.readline()
                    categories = header.split(',')
                    price_index = categories.index("price")
                    room_index = categories.index('room_id')
                    room_type_index = categories.index('room_type')

                    for line in file_in:
                        stats = line.split(',')
                        if len(stats) == len(categories):
                            room_id = stats[room_index]
                            price = stats[price_index]
                            if stats[room_type_index] == roomtype and room_id != "" and price != "":
                                if room_id in price_dict \
                                        and price_dict[room_id] is not None \
                                        and price_dict[room_id] != "":
                                    price_dict[room_id].append(float(price))
                                else:
                                    price_dict[room_id] = [price]
                break

    return price_dict


def price_change(d):
    """
    This function takes as input a dictionary in the format returned from room_prices (Func-
    tion 3) and returns a tuple with three elements in the following order:
    â€¢ maximum percentage change (which could be either positive or negative) for the set
        of properties in the dictionary
    â€¢ the starting price for the property that has the maximum percentage change
    â€¢ the ending price for the property that has the maximum percentage change

    :param d: dictionary from room_prices
    :return big_change, start_price, end_price: touple with the larges percent change and matching values
    """
    big_change = 0
    for key in d:
        price_list = d[key]
        if len(price_list) > 1:
            percent_change = (int(float(price_list[-1])) - int(float(price_list[0]))) / int(float(price_list[0]))
            if abs(percent_change) > abs(big_change):
                big_change = percent_change
                start_price = price_list[0]
                end_price = price_list[-1]
    # Returns zeros if there is no readable data
    if start_price is None:
        start_price = 0
        end_price = 0
        big_change = 0
    return (big_change * 100), float(start_price), float(end_price)


def price_by_neighborhood(filename):
    """
    This function takes a string containing the name of a file.
    It creates and returns a dictionary where each key is a neighborhood (str)
    that appears in the file and the value for a key is the average price for
    an â€œEntire home/aptâ€ listing in that neighborhood.

    :param filename: input file with airbnb data
    :return nbrhood_dict: a dictionary containing neighborhoods and their average listing prices
    """

    with open(filename, "r", encoding="utf-8") as file_in:
        nbrhood_dict = {}
        prices_count = {}
        header = file_in.readline()
        categories = header.split(',')
        price_index = categories.index("price")
        room_type_index = categories.index('room_type')
        neighbor_index = categories.index('neighborhood')
        length = len(categories)

        for line in file_in:
            stats = line.split(',')
            neighborhood = stats[neighbor_index]
            price = stats[price_index]
            if price != "price" and len(stats) == length:
                if stats[room_type_index] == 'Entire home/apt' and price != "" and neighborhood != "":
                    # stores sum of prices in nbrhood and the price count in prices_count
                    if neighborhood in nbrhood_dict:
                        nbrhood_dict[neighborhood] = float(nbrhood_dict.get(neighborhood)) + float(price)
                        prices_count[neighborhood] += 1
                    else:
                        nbrhood_dict[neighborhood] = price
                        prices_count[neighborhood] = 1
        for key in nbrhood_dict:
            # finds the averages for each neighborhood
            nbrhood_dict[key] = nbrhood_dict.get(key) / prices_count.get(key)
        return nbrhood_dict


def plot_data(filename):
    """
    This plot takes the price and rating pairs from the price_satisfaction function
    and then finds the percentages of three price range categories filtered according to
    high, medium, or low ratings. The chart shows the differences in rating distribution
    among varying prices and allows us to visualize if there is a correlation between
    price and average rating.

    :param filename: str that is name of file
    """
    price_rating = price_satisfaction(filename)

    price = []
    rating = []
    for list_num in range(len(price_rating)):
        price.append(price_rating[list_num][0])
        rating.append(price_rating[list_num][1])
    low_low = 0
    low_med = 0
    low_high = 0
    total_low = 0
    med_low = 0
    med_med = 0
    med_high = 0
    total_med = 0
    high_low = 0
    high_med = 0
    high_high = 0
    total_high = 0

    # This divides the info into rating categories and then separates that even further into price categories
    for i in range(len(price_rating)):
        if rating[i] == '0.0' or rating[i] == '0.5' or rating[i] == '1.0' or rating[i] == '1.5' or rating[i] == '2.0':
            if 0 < int(float(price[i])) < 60:
                low_low = low_low + 1
                total_low = total_low + 1
            elif 60 < int(float(price[i])) < 120 or int(float(price[i])) == 60:
                med_low = med_low + 1
                total_med = total_med + 1
            else:
                high_low = high_low + 1
                total_high = total_high + 1
        elif rating[i] == '2.5' or rating[i] == '3.0' or rating[i] == '3.5':
            if 0 < int(float(price[i])) < 60:
                low_med = low_med + 1
                total_low = total_low + 1
            elif 60 < int(float(price[i])) < 120 or int(float(price[i])) == 60:
                med_med = med_med + 1
                total_med = total_med + 1
            else:
                high_med = high_med + 1
                total_high = total_high + 1
        elif rating[i] == '4.0' or rating[i] == '4.5' or rating[i] == '5.0':
            if 0 < int(float(price[i])) < 60:
                low_high = low_high + 1
                total_low = total_low + 1
            elif 60 < int(float(price[i])) < 120 or int(float(price[i])) == 60:
                med_high = med_high + 1
                total_med = total_med + 1
            else:
                high_high = high_high + 1
                total_high = total_high + 1
    print(total_med)
    print(total_low)
    print(total_high)

    # This finds the percentages for each price category
    lows = [round((low_low / total_low) * 100, 2),
            round((low_med / total_low) * 100, 2),
            round((low_high / total_low) * 100, 2)]
    mediums = [round((med_low / total_med) * 100, 2),
               round((med_med / total_med) * 100, 2),
               round((med_high / total_med) * 100, 2)]
    highs = [round((high_low / total_high) * 100, 2),
             round((high_med / total_high) * 100, 2),
             round((high_high / total_high) * 100, 2)]

    # These are all specs for the plot
    x = ["0-2", "2.5-3.5", "4-5"]
    lows = plt.plot(x, lows, label='0-60')
    plt.setp(lows, color='r')
    plt.plot(x, mediums, label='60-120')
    plt.plot(x, highs, label='>120')

    # Labels!
    plt.ylabel('Percent of Houses')
    plt.xlabel('Ratings')
    plt.title('Distribution of Prices based on the Rating')

    plt.legend()
    plt.show()


def main():
    filename = 'tomslee_airbnb_brno_1099_2017-04-12.csv'
    plot_data(filename)


main()