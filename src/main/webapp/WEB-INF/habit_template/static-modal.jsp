<%--
  Created by IntelliJ IDEA.
  User: NAMBACH
  Date: 7/1/2018
  Time: 9:59 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<button id="btnStaticModal" type="button" class="btn btn-secondary mb-1 hidden" data-toggle="modal"
        data-target="#staticModal">
    Open Modal
</button>

<div class="modal fade" id="staticModal" tabindex="-1" role="dialog" aria-labelledby="staticModalLabel"
     data-backdrop="static" style="display: none;" aria-hidden="true">
    <div class="modal-dialog modal-md" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="staticModalLabel">Static Modal</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">×</span>
                </button>
            </div>
            <div class="modal-body">

                <form id="staticModalForm">
                    <div class="form-group">
                        <label for="inputUsername" class="form-control-label">
                            Username
                        </label>
                        <input id="inputUsername" class="form-control" type="text">
                    </div>

                    <div class="form-group">
                        <label for="inputFullName" class="form-control-label">
                            Full Name
                        </label>
                        <input id="inputFullName" class="form-control" type="text">
                    </div>

                    <div class="form-group">
                        <label for="inputInformation" class="form-control-label">
                            Information
                        </label>
                        <input id="inputInformation" class="form-control" type="text">
                    </div>

                    <div class="form-group">
                        <label for="inputInformation" class="form-control-label">
                            Information
                        </label>
                        <input id="inputFile" class="form-control" type="file">
                        <br/>
                        <img id="inputFileImg" src="">
                    </div>
                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                <button id="btnSubmit" type="button" class="btn btn-primary">Submit</button>
            </div>
        </div>
    </div>
</div>
