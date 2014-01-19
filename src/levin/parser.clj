; This file is part of levin.
;
; levin is free software: you can redistribute it and/or modify
; it under the terms of the GNU Lesser General Public License as published by
; the Free Software Foundation, either version 3 of the License, or
; (at your option) any later version.
;
; Foobar is distributed in the hope that it will be useful,
; but WITHOUT ANY WARRANTY; without even the implied warranty of
; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
; GNU Lesser General Public License for more details.
;
; You should have received a copy of the GNU Lesser General Public License
; along with Foobar.  If not, see <http://www.gnu.org/licenses/>.

(ns levin.parser
  (:require [clojure.string :as str]
            [levin.clipping :as clipping]))

(def title-and-author-re #"^(.+) \((.+)\)$")
(def type-location-and-date-re #"(?i)^-\s(?:Your\s)?(\w+) (?:on page ([0-9-]*?) \| )?(?:Loc(?:ation|\.) ([0-9-]*?) +\| )?Added on (.*)$")

(defn- filter-present
  "Filters only fields which have values"
  [fields]
  (filter (fn [[k v]] (not (nil? v)))
          fields))

(defn parse-clipping-book-info
  "Parses out title and possible author from the first
   line of the clipping"
  [str]
  (if-let [[match title author] (re-matches title-and-author-re str)]
    { :title title :author author }
    { :title str }))

(defn parse-clipping-type-location-and-date
  "Parses type, location and date of creation from the
   second line of the clipping"
  [str]
  (if-let [[match type page location date] (re-matches type-location-and-date-re str)]
    [[:type (keyword (str/lower-case type))] [:location location] [:added-on date] [:page page]]
    []))

(defn parse-clipping
  "Parses one clipping out of string"
  [str]
  (let [[first second & notes] (str/split-lines str)
        ;; there is an empty line between meta data and content
        content (str/join (rest notes))]
    (clipping/build (parse-clipping-book-info first)
                    (conj
                     (parse-clipping-type-location-and-date second)
                     [:content content]))))
