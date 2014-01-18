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

(ns levin.clipping)

(defn- tagged-with?
  "Checks if map has type key and if it's value matches expected"
  [expected-type clipping]
  (and (contains? clipping :type)
       (= expected-type (:type clipping))))

(defn bookmark? [clipping]
  (tagged-with? :bookmark clipping))

(defn highlight? [clipping]
  (tagged-with? :highlight clipping))

(defn build
  "Build clipping of specified type from a sequence of fields"
  [type fields]
  (reduce (fn [map kv] (apply assoc map kv))
          {:type type}
          fields))
