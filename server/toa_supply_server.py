from flask import Flask, request, jsonify
from threading import Lock
from toa_supply_group import ToaSupplyGroup
import time

app = Flask(__name__)

open_groups_lock = Lock()
open_groups: dict[str, int] = {}


@app.route('/toa_supply', methods=['POST'])
def match_request():
    body = request.get_json()

    tmp_str = str(body)
    with open_groups_lock:
        if tmp_str not in open_groups:
            open_groups[tmp_str] = 0
        open_groups[tmp_str] += 1

    for _ in range(30 * 60 * 10):
        with open_groups_lock:
            group = open_groups[tmp_str]
            if group == 2:
                return jsonify({"message": "We did it", "body": body})

        time.sleep(0.1)

    return jsonify({"message": "Nope sorry", "body": body})


#     with lock:
#         if body not in requests_by_body:
#             requests_by_body[body] = []
#         requests_by_body[body].append(request)
#
#         if len(requests_by_body[body]) == 1:
#             response = jsonify({"message": "Match found!", "body": body})
#             for req in requests_by_body[body]:
#                 req.environ['werkzeug.server.shutdown']()  # Simulate responding to all
#             requests_by_body.pop(body)  # Clear storage for this body
#             return response

if __name__ == '__main__':
    app.run(threaded=True)
