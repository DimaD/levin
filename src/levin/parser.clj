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

(defn parse-clipping-title-and-author
  "Parses out title and possible author from the first
   line of the clipping"
  [str]
  (if-let [[match title author] (re-matches #"^(.+) \((.+)\)$" str)]
    [[:title title] [:author author]]
    [[:title str]]))

(defn parse-clipping
  "Parses one clipping out of string"
  [str]
  (let [lines (str/split-lines str)
        title-and-author (parse-clipping-title-and-author (first lines))
        note (rest lines)]
    (clipping/build :bookmark (concat title-and-author note))))
